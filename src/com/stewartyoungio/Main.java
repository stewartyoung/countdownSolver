package com.stewartyoungio;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static double target;
    static double closestResult;
    static String closestEquation = null;
    static Integer combinationsTried = 0;

    public static void main(String[] args) {
        List<Double> numbers = new ArrayList<Double>();
        for (int i=0; i<6; i++) {
            numbers.add(Double.parseDouble(args[i]));
        }

        target = Double.parseDouble(args[6]);

        // generate all possible permutations of the list
            // there are n! possible permutations of a list of size n
        List<List<Double>> permutations = generatePermutations(numbers);

        for (List<Double> p : permutations) {
            solve(p, null, 0, false);
        }

        if (closestResult == target) {
            System.out.println("Exact solution:");
            System.out.println(prettify(closestEquation));
            System.out.println("combinations tried:" + combinationsTried.toString());
        }
        else {
            System.out.println("No exact solution. Closest was:");
            System.out.println(prettify(closestEquation) +" = " +closestResult);
            System.out.println("combinations tried:" + combinationsTried.toString());
        }
    }

    public static String prettify(String equation) {
        return equation.replace(".0", "").replaceAll("^\\((.+?)\\)$", "$1");
    }

    public static void solve(List<Double> list, String equation, double total, boolean lastOperatorWasWeak) {
        System.out.println(equation);
        if (list.size() == 0) {
            return;
        }

        // update closest equation and result
            // of we are closer to the target now than the last closest result
        if ((total == target) || (Math.abs(total-target) < Math.abs(closestResult-target))) {

            if (closestResult != total) {
                closestEquation = null;
            }
            if ((closestEquation == null) || (equation.length() < closestEquation.length())) {
                closestEquation = equation;
                closestResult = total;
            }
        }

        List<Double> newList = new ArrayList<Double>(list);
        // take first element off the list and try and solve with it
        Double f = newList.remove(0);
        if (equation == null) {
            solve(newList, ""+f, f, false);
            combinationsTried+=1;
        }
        else {
            solve(newList, equation +" + " +f, total + f, true);
            combinationsTried++;
            solve(newList, equation +" - " +f, total - f, true);
            combinationsTried++;

            if (lastOperatorWasWeak) {
                equation = "(" +equation +")";
                System.out.println(equation);
            }
            solve(newList, equation +" * " +f, total * f, false);
            combinationsTried++;
            solve(newList, equation +" / " +f, total / f, false);
            combinationsTried++;
        }
    }

    public static List<List<Double>> generatePermutations(List<Double> list) {
        if (list.size() == 0) {
            ArrayList<List<Double>> result = new ArrayList<List<Double>>();
            result.add(new ArrayList<Double>());
            return result;
        }

        Double insert = list.remove(0);
        List<List<Double>> result = new ArrayList<List<Double>>();
        List<List<Double>> permutations = generatePermutations(list);
        for (List<Double> permutation : permutations) {
            for (int i=0; i <= permutation.size(); i++) {
                List<Double> temp = new ArrayList<Double>(permutation);
                // add [:-i] in temporary list to index i
                temp.add(i, insert);
                result.add(temp);
            }
        }
        return result;
    }
}
