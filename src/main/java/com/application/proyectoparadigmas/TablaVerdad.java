package com.application.proyectoparadigmas;

import com.application.proyectoparadigmas.EquationVariables;

import java.util.ArrayList;


public class TablaVerdad {
    ArrayList<EquationVariables> variables = new ArrayList<>();
    ArrayList<Object> equation;
    ArrayList<Object> temp;
    String tabla = "";

    public TablaVerdad(ArrayList<EquationVariables> vars, ArrayList<Object> theEquation){
        variables = vars;
        equation = theEquation;
    }

    /**
     * The method that constructs the truth table
     * It goes through every possible binary combination for the variables, and calls parseEquation()
     * for each one. If parseEquation returns false, the program stops executing
     */
    public void constructTable(){
        //prints out the top row of the truth table
        for (EquationVariables variable : variables) {
            tabla += " | " + variable.getName();
        }
        tabla += " || " + equation.toString() + '\n';

        //Goes through every possible combination of values for the variables and prints them
        for (int i = 1; i < Math.pow(2, variables.size())+1; i++){
            int size = variables.size();
            //Goes through all the variables
            for (int m = size-1; m >= 0; m--){
                //if the remainder of i divided by the variables significant bit = 1
                if (i%variables.get(m).getSigBit()==1){
                    //set the state
                    variables.get(m).setState();
                }
                //if the significant bit = 1, also switch state
                if (variables.get(m).getSigBit() == 1){
                    variables.get(m).setState();
                }
                tabla +=" | " + (variables.get(m).getState() ? 1 : 0);
            }
            tabla +=" || ";
            //attempts to parse the equation for the current variable states.
            //If it fails, end the program
            if (!parseEquation()){
                return;
            }
        }
    }

    public void invertVal(int pos){
        if ((Integer)temp.get(pos)==0){
            temp.set(pos, 1);
        }else{
            temp.set(pos, 0);
        }
    }

    public void orValues(int leftPos, int rightPos){
        if ((Integer)temp.get(leftPos) == 1 || (Integer) temp.get(rightPos) == 1){
            temp.set(rightPos, 1);
        }else{
            temp.set(rightPos, 0);
        }
    }

    public void andValues(int leftPos, int rightPos){
        if ((Integer) temp.get(leftPos) == 1 && ((Integer) temp.get(rightPos)) == 1){
            temp.set(rightPos, 1);
        }else{
            temp.set(rightPos, 0);
        }
    }

    public boolean isInt(int pos){
        return temp.get(pos).getClass().equals(Integer.class);
    }

    /**
     * Goes through the equation and performs the calculations.
     * @return true if the equation is properly formatted, false otherwise
     */
    public boolean parseEquation(){
        temp = new ArrayList<>(equation);
        for (int j = 0; j < temp.size();j++){
            if (temp.get(j).getClass().equals(EquationVariables.class)){
                temp.set(j, ((EquationVariables)temp.get(j)).getState() ? 1 : 0);
            }
        }
        for (int i = 0; i < temp.size(); i++){
            if (temp.get(i).getClass().equals(Character.class)){
                if (temp.get(i).equals('!') && isInt(i+1)){
                    invertVal(i+1);
                }else if ((!(i == 0) && !(i == temp.size()-1)) && isInt(i-1)){
                    if (isInt(i+1)){
                        if (temp.get(i).equals('+')){
                            orValues(i-1, i+1);
                        }else if (temp.get(i).equals('*')){
                            andValues(i-1, i+1);
                        }
                    }else if (temp.get(i+1).equals('!') && isInt(i+2)){
                        invertVal(i+2);
                        if (temp.get(i).equals('+')){
                            orValues(i-1, i+2);
                        }else if (temp.get(i).equals('*')){
                            andValues(i-1, i+2);
                        }
                        temp.remove(i+1);
                    }else{
                        return fail();
                    }
                }else {
                    return fail();
                }
                //If two integers are next to each other, end the program for an improper equation
            }else if (isInt(i) && i < temp.size()-1 && isInt(i+1)){
                return fail();
            }
        }
        //prints out the solution in the last column of the truth table

        tabla +=temp.get(temp.size()-1);
        tabla +='\n';
        return true;
    }
    /*
     * This method prints an error method and returns false to parseEquation(), ending the program
     */
    private boolean fail(){
        tabla ="Invalid String";
        return false;
    }
}