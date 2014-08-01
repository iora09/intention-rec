package com.LPSWorkflow.common;

import com.LPSWorkflow.model.abstractComponent.Comparison;
import com.LPSWorkflow.model.abstractComponent.Entity;
import com.LPSWorkflow.model.abstractComponent.Fluent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper methods for dealing with facts
 */
public class FactsHelper {
    public static boolean isVariable(String str){
        if(str == null || str.isEmpty() || str.length() == 0){
            return false;
        }
        char firstChar = str.charAt(0);
        return !Character.isDigit(firstChar) && Character.isUpperCase(firstChar);
    }

    public static boolean hasNoTerm(String str){
        return !isComparison(str) && !str.contains("(");
    }

    public static boolean isAlreadyBound(String str){
        if(str == null){
            return false;
        }
        List<String> terms = extractTerms(str);
        return (terms == null || terms.isEmpty())
                || terms.stream().noneMatch(FactsHelper::isVariable);
    }

    public static boolean isComparison(String str) {
        return str.contains("==")
                || str.contains("!=")
                || str.contains("<")
                || str.contains(">")
                || str.contains("<=")
                || str.contains(">=");
    }

    private static boolean compare(String name) {
        List<String> ops = Arrays.asList("==", "!=", "<", ">", "<=", ">=");

        for(String op : ops){
            if(name.contains(op)){
                String[] split = name.replaceAll("\\s","").split(op); // remove whitespace and split
                if(split.length < 2){
                    return false;
                }
                String name1 = split[0];
                String name2 = split[1];
                return compare(name1, op, name2);
            }
        }
        return false;
    }

    private static boolean compare(String name1, String op, String name2) {
        switch (op) {
            case "==":
                return name1.equals(name2);
            case "!=":
                return !name1.equals(name2);
            default:
                if (isVariable(name1) || isVariable(name2)) {
                    return false; // cannot compare variables numerically
                } else if (op.equals("<")) {
                    return Integer.parseInt(name1) < Integer.parseInt(name2);
                } else if (op.equals(">")) {
                    return Integer.parseInt(name1) > Integer.parseInt(name2);
                } else if (op.equals("<=")) {
                    return Integer.parseInt(name1) <= Integer.parseInt(name2);
                } else if (op.equals(">=")) {
                    return Integer.parseInt(name1) >= Integer.parseInt(name2);
                }
                break;
        }
        return false;
    }

    public static boolean holds(Entity entity, Map<String, String> binding, Collection<String> facts) {
        // checks if the entity holds in given facts with given binding
        if(entity == null){
            return false;
        }

        if(entity.getType() == EntityType.FLUENT){
            Fluent fluent = (Fluent) entity;
            List<String> names = fluent.getNames();
            Map<String, List<String>> termsMap = fluent.getTermsMap();
            return holdsAll(names.stream().map(n -> buildFullName(n, termsMap.get(n), binding)).collect(Collectors.toList()), facts);
        } else if(entity.getType() == EntityType.COMPARISON) {
            String name1 = ((Comparison) entity).getName1();
            String op = ((Comparison) entity).getOp();
            String name2 = ((Comparison) entity).getName2();
            String boundName1 = isVariable(name1) ? binding.get(name1) : name1;
            String boundName2 = isVariable(name2) ? binding.get(name2) : name2;
            return compare(boundName1, op, boundName2);
        } else {
            String name = entity.getName();
            List<String> terms = entity.getTerms();
            if(terms == null){
                return holds(name, null, facts, true);
            }
            return holds(name, terms.stream().map(binding::get).collect(Collectors.toList()), facts, true);
        }
    }

    public static boolean holds(String nameWithTerms, Collection<String> facts, boolean forSpecificBinding){
        if(isComparison(nameWithTerms)){
            return compare(nameWithTerms);
        }
        String name = extractNamePart(nameWithTerms);
        List<String> terms = extractTerms(nameWithTerms);
        return holds(name, terms, facts, forSpecificBinding);
    }

    public static boolean holds(String name, List<String> terms, Collection<String> facts, boolean forSpecificBinding) {
        if(name == null || facts == null){
            return false;
        }
        if(terms == null){
            // no terms, so facts simple contains it if true
            return facts.contains(name);
        }

        String targetName = name.contains("!") ? name.substring(1) : name;
        boolean contains = facts.stream().anyMatch(fact -> {   // fact => factName(t1, t2, t3 ...)
            String factName = extractNamePart(fact);
            List<String> factTerms = extractTerms(fact);
            return !(factName == null || factTerms == null)
                    && areMatching(targetName, terms, factName, factTerms, forSpecificBinding);
        });

        if (name.contains("!")) {
            return !contains;
        } else {
            return contains;
        }
    }

    // check if all of the predicates in the body holds (for any binding )
    public static boolean holdsAll(Collection<String> body, Collection<String> facts) {
        if(body.stream().anyMatch(b -> (hasNoTerm(b) || isAlreadyBound(b)) && !holds(b, facts, true))){
            // if any of the 0-ary predicates does not hold, then body does not hold
            return false;
        }
        if(body.stream().allMatch(b -> (hasNoTerm(b) || isAlreadyBound(b)) && holds(b, facts, true))){
            return true; // does not need binding, and all are true
        }

        // we do not need to bind already bound predicates
        ArrayList<String> bodyToEvaluate = new ArrayList<>(body);
        bodyToEvaluate.removeIf(b -> hasNoTerm(b) || isAlreadyBound(b));
        List<Map<String, String>> bindings = getValidBindings(bodyToEvaluate, facts);
        return  bodyToEvaluate.isEmpty() // nothing to check
                || (bindings != null && bindings.size() > 0); // there is a valid binding
    }

    public static boolean areMatching(String srcNameWithTerms, String targetNameWithTerms, boolean specific) {
        return areMatching(extractNamePart(srcNameWithTerms), extractTerms(srcNameWithTerms),
                extractNamePart(targetNameWithTerms), extractTerms(targetNameWithTerms), specific);
    }

    private static boolean areMatching(String srcName, List<String> srcTerms,
                                       String targetName, List<String> targetTerms,
                                       boolean specific) {
        if(!srcName.equals(targetName)
                || (srcTerms == null && targetTerms != null)
                || (srcTerms != null && targetTerms == null)){
            return false;
        }
        if(srcTerms == null && targetTerms == null){
            return true;
        }

        if(srcTerms.size() == targetTerms.size()){
            if(specific){
                for (int i = 0; i < srcTerms.size(); i++) {
                    if (!isVariable(srcTerms.get(i)) && !isVariable(targetTerms.get(i))
                            && !srcTerms.get(i).equals(targetTerms.get(i))) {
                        return false;
                    }
                }
                return true; // all terms match exactly
            }
            return true; // terms match, and no need to be specific
        }
        return false; // name or terms are different
    }

    private static List<Map<String, String>> mergeBindings(List<String> predicates,
                                                           List<List<Map<String, String>>> listOfBindings,
                                                           Collection<String> facts) {
        // using bindings for multiple fluents (Concurrent), evaluate possible merged bindings
        List<Map<String, String>> resultBindings = new ArrayList<>();
        if(listOfBindings.stream().anyMatch(bindings -> bindings == null || bindings.isEmpty())){
            return resultBindings; // return empty list since some fluents do not have any valid bindings
        }

        Set<String> variables = new HashSet<>();
        for (List<Map<String,String>> bindings : listOfBindings){
            bindings.forEach(b -> variables.addAll(b.keySet()));
        }

        Map<String, Set<String>> bindingPerVariable = new HashMap<>();
        // for each variable, discard invalid bindings from the list
        variables.forEach(variable -> {
            List<List<Map<String, String>>> relevantBindings = listOfBindings.stream()
                    .filter(bindings -> bindings.stream().anyMatch(binding -> binding.containsKey(variable)))
                    .collect(Collectors.toList());

            relevantBindings.forEach(bindings -> {
                Set<String> values = new HashSet<>(); // possible values for the variable in this list
                bindings.forEach(binding -> values.add(binding.get(variable)));
                relevantBindings.stream().filter(otherBindings -> !otherBindings.equals(bindings))
                        .forEach(otherBindings -> {
                            // discard bindings that are not possible in merged settings
                            otherBindings.removeIf(binding -> !values.contains(binding.get(variable)));
                        });
            });


            // find list of bindings that contains the binding for the particular variable
            List<Set<String>> possibleValuesFromEachBindings = relevantBindings.stream()
                    .map(bindings -> {
                        // get all binding values that exists for the variable
                        Set<String> values = new HashSet<>();
                        bindings.forEach(binding -> values.add(binding.get(variable)));
                        return values;
                    })
                    .collect(Collectors.toList());
            // if there is any, add the possible bindings for the variable that exists in all lists
            if(!possibleValuesFromEachBindings.isEmpty()){
                Set<String> possibleValues = new HashSet<>(possibleValuesFromEachBindings.get(0));
                possibleValuesFromEachBindings.forEach(possibleValues::retainAll);
                bindingPerVariable.put(variable, possibleValues);
            }
        });

        // using the permutation of possible values of each variable, make a list of bindings
        // note: for n variables each with m possibilities, there are m^3 possible permutations.
        bindingPerVariable.forEach((variable, values) -> {
            if(values.isEmpty()){
                return; // no binding values to be added
            }
            if(resultBindings.isEmpty()){
                values.forEach(value -> {
                    Map<String, String> binding = new HashMap<>();
                    binding.put(variable, value);
                    resultBindings.add(binding);
                });
            } else {
                List<Map<String,String>> toBeAdded = new ArrayList<>();
                List<Map<String,String>> toBeRemoved = new ArrayList<>();
                resultBindings.forEach(binding -> {
                    values.forEach(value -> {
                        Map<String, String> newBinding = new HashMap<>(binding);
                        newBinding.put(variable, value);
                        toBeAdded.add(newBinding);
                    });
                    toBeRemoved.add(binding);
                });
                resultBindings.addAll(toBeAdded);
                resultBindings.removeAll(toBeRemoved);
            }

        });

        // final check if the merged binding actually holds
        return resultBindings.stream().filter(binding -> {
            for(String predicate : predicates){
                if(isComparison(predicate)){
                    if(!compare(substituteTerms(predicate, binding))){
                        return false;
                    }
                } else {
                    if(!holds(substituteTerms(predicate, binding), facts, true)){
                        return false;
                    }
                }
            }
            return true;})
            .collect(Collectors.toList());
    }

    public static List<Map<String, String>> getValidBindings(Entity entity, Collection<String> facts) {
        if(entity == null){
            return null;
        }

        if(entity.getType() == EntityType.FLUENT){
            Fluent fluent = (Fluent) entity;
            List<String> names = fluent.getNames();
            Map<String, List<String>> termsMap = fluent.getTermsMap();

            List<String> predicates = names.stream()
                    .map(name -> buildFullName(name, termsMap.get(name)))
                    .filter(predicate -> !hasNoTerm(predicate)
                            && !isAlreadyBound(predicate)) // filter out already true predicates and 0-ary predicates
                    .collect(Collectors.toList());

            List<List<Map<String, String>>> listOfBindings = predicates.stream()
                    .map(predicate -> getValidBindings(predicate, facts))
                    .filter(bindings -> bindings != null && !bindings.isEmpty())
                    .collect(Collectors.toList());
            return mergeBindings(predicates, listOfBindings, facts);
        } else {
            String name = entity.getName();
            List<String> terms = entity.getTerms();
            return getValidBindings(name, terms, facts);
        }
    }

    public static List<Map<String, String>> getValidBindings(String nameWithTerms, Collection<String> facts){
        if(isAlreadyBound(nameWithTerms)){
            return null;
        }
        String name = extractNamePart(nameWithTerms);
        List<String> terms = extractTerms(nameWithTerms);
        return getValidBindings(name, terms, facts);
    }

    private static List<Map<String, String>> getValidBindings(String name, List<String> terms, Collection<String> facts) {
        if(name == null || terms == null || facts == null){
            // (if terms are null, there is no available binding)
            return null;
        }

        List<Map<String, String>> result = new ArrayList<>();

        String targetName = name.contains("!") ? name.substring(1) : name;
        facts.stream().forEach(fact -> {   // fact => factName(t1, t2, t3 ...)
            String factName = extractNamePart(fact);
            List<String> factTerms = extractTerms(fact);
            if (factName != null && factTerms != null) {
                if (name.contains("!")) {
                    // Do nothing for negation, since we do not know what to bind
                } else {
                    if (areMatching(targetName, terms, factName, factTerms, false)) {
                        Map<String, String> bindings = new HashMap<>();
                        for(int i = 0; i < terms.size(); i++){
                            if(!terms.get(i).equals(factTerms.get(i)) && isVariable(terms.get(i))){ // if e.g. X -> X, ignore it
                                bindings.put(terms.get(i), factTerms.get(i));
                            }
                        }
                        if(bindings.size() > 0){ // if size == 0, then there is no information
                            result.add(bindings);
                        }
                    }
                }
            }
        });
        return result;
    }

    public static List<Map<String, String>> getValidBindings(List<String> body, Collection<String> facts) {
        if(body == null || facts == null || body.isEmpty()
                || body.stream().anyMatch(b -> (hasNoTerm(b) || isAlreadyBound(b)) && !facts.contains(b))) {
                // there is a predicate that prevents the body from holding.
            return null;
        }

        // use only positive predicates to look for valid bindings, then check using body later.
        List<String> positivePredicates = body.stream()
                .filter(b -> !b.contains("!") && !isComparison(b))
                .collect(Collectors.toList());
        if(!positivePredicates.isEmpty()){
            List<List<Map<String, String>>> listOfBindings = positivePredicates.stream()
                    .map(p -> getValidBindings(p, facts))
                    .filter(binding -> binding != null && !binding.isEmpty())
                    .collect(Collectors.toList());
            // return all valid bindings
            return mergeBindings(body, listOfBindings, facts);
        } else {
            return null; // only negatives in body... cannot check bindings
        }
    }

    private static String extractNamePart(String nameWithTerms){
        if(nameWithTerms == null){
            return null;
        }
        if(hasNoTerm(nameWithTerms) || isComparison(nameWithTerms)){
            return nameWithTerms; // 0-ary predicate or Comparison
        }
        String[] split1 = nameWithTerms.split("\\(");
        if (split1.length < 2) {
            return null; // invalid nameWithTerms
        }
        return split1[0];
    }

    public static List<String> extractTerms(String nameWithTerms){
        if(nameWithTerms == null){
            return null;
        }
        if(isComparison(nameWithTerms)){
            List<String> ops = Arrays.asList("==", "!=", "<", ">", "<=", ">=");

            for(String op : ops){
                if(nameWithTerms.contains(op)){
                    String[] split = nameWithTerms.replaceAll("\\s","").split(op); // remove whitespace and split
                    if(split.length < 2){
                        return null;
                    }
                    String name1 = split[0];
                    String name2 = split[1];
                    return Arrays.asList(name1, name2);
                }
            }
            return null;
        }
        String[] split1 = nameWithTerms.split("\\(");
        if (split1.length < 2) {
            return null; // invalid nameWithTerms or 0-ary
        }

        String[] split2 = split1[1].split("\\)");
        if (!split1[1].contains(")") || split2.length < 1) {
            return null;
        }

        return Arrays.asList(split2[0].replaceAll("\\s","").split(","));
    }

    private static Map<String, String> createDefaultBinding(Entity entity) {
        Map<String, String> binding = new HashMap<>();
        entity.getTerms().forEach(t -> binding.put(t, t));
        return binding;
    }

    public static String buildFullName(Entity e, Map<String, String> binding) {
        if(e == null){
            return null;
        }

        if(e.getType() == EntityType.FLUENT) {
            Fluent fluent = (Fluent) e;
            List<String> names = fluent.getNames();
            Map<String, List<String>> termsMap = fluent.getTermsMap();

            return String.join(":", names.stream()
                    .map(name -> {
                        List<String> terms = termsMap.get(name);
                        if(terms == null){
                            // it is a 0-ary predicate
                            return name;
                        } else {
                            return buildFullName(name, terms.stream().map(binding::get).collect(Collectors.toList()));
                        }
                    })
                    .collect(Collectors.toList()));
        } else {
            return buildFullName(e.getName(), e.getTerms(), binding);
        }
    }

    public static String buildFullName(Entity e) {
        return buildFullName(e, createDefaultBinding(e));
    }

    public static String buildFullName(String nameWithTerms, Map<String, String> binding) {
        return buildFullName(extractNamePart(nameWithTerms), extractTerms(nameWithTerms), binding);
    }

    private static String buildFullName(String name, List<String> terms, Map<String, String> binding) {
        if(terms == null){
            return name;
        }
        return buildFullName(name, terms.stream().map(binding::get).collect(Collectors.toList()));
    }

    public static String buildFullName(String name, List<String> terms) {
        if(terms == null){
            return name;
        }
        return String.format("%s(%s)", name, String.join(",", terms));
    }



    public static Map<String, String> extractBinding(String unboundName, String boundName){
        Map<String, String> resultBinding = new HashMap<>();
        if(isAlreadyBound(unboundName)){
            return resultBinding;
        }
        if(isComparison(unboundName)){
            List<String> ops = Arrays.asList("==", "!=", "<", ">", "<=", ">=");
            for(String op : ops) {
                if (unboundName.contains(op)) {
                    String[] unboundSplit = unboundName.replaceAll("\\s","").split(op); // remove whitespace and split
                    String[] boundSplit = boundName.replaceAll("\\s","").split(op);
                    if(unboundSplit.length < 2 || boundSplit.length < 2){
                        break;
                    }
                    String unboundName1 = unboundSplit[0];
                    String unboundName2 = unboundSplit[1];
                    String boundName1 = boundSplit[0];
                    String boundName2 = boundSplit[1];
                    if(isVariable(unboundName1)){
                        resultBinding.put(unboundName1, boundName1);
                    }
                    if(isVariable(unboundName2)){
                        resultBinding.put(unboundName2, boundName2);
                    }
                    break;
                }
            }
            return resultBinding;
        } else {
            List<String> unboundTerms = extractTerms(unboundName);
            List<String> boundTerms = extractTerms(boundName);
            for (int i = 0; i < unboundTerms.size(); i++) {
                resultBinding.put(unboundTerms.get(i), boundTerms.get(i));
            }
            return resultBinding;
        }
    }

    public static List<String> getBoundNames(String boundName, List<String> names) {
        // bind variables as far as possible using already bound name

        Optional<String> match = names.stream().filter(n -> areMatching(n, boundName, false)).findAny();
        if(!match.isPresent()){
            return names.stream().filter(n -> !n.equals(boundName)).collect(Collectors.toList());
        }

        String unboundName = match.get();
        if(isAlreadyBound(unboundName)){
            // nothing to bind
            if(unboundName.equals(boundName)){
                return names.stream().filter(n -> !n.equals(unboundName)).collect(Collectors.toList());
            } else {
                // names does not contain matching element for boundName
                return null;
            }
        }

        Map<String, String> binding = extractBinding(unboundName, boundName);
        // bind each terms and return
        return names.stream().filter(n -> !n.equals(unboundName))
                .map(n -> substituteTerms(n, binding))
                .collect(Collectors.toList());
    }

    public static String substituteTerms(String nameWithTerms, Map<String, String> binding) {
        if(binding == null || binding.isEmpty()
                || isAlreadyBound(nameWithTerms)){
            return nameWithTerms;
        }
        if(isComparison(nameWithTerms)){
            List<String> ops = Arrays.asList("==", "!=", "<", ">", "<=", ">=");
            for(String op : ops) {
                if (nameWithTerms.contains(op)) {
                    String[] split = nameWithTerms.replaceAll("\\s","").split(op); // remove whitespace and split
                    if(split.length < 2){
                        return nameWithTerms;
                    }
                    String name1 = split[0];
                    String name2 = split[1];
                    if(isVariable(name1) && binding.get(name1) != null){
                        name1 = binding.get(name1);
                    }
                    if(isVariable(name2) && binding.get(name2) != null){
                        name2 = binding.get(name2);
                    }
                    return name1 + op + name2;
                }
            }
            return null;
        } else {
            String namePart = extractNamePart(nameWithTerms);
            List<String> terms = extractTerms(nameWithTerms);
            List<String> newTerms = new ArrayList<>();
            for (int i = 0; i < terms.size(); i++) {
                String term = terms.get(i);
                if (binding.containsKey(term)) {
                    newTerms.add(i, binding.get(term));
                } else {
                    newTerms.add(i, term);
                }
            }
            return buildFullName(namePart, newTerms);
        }
    }
}
