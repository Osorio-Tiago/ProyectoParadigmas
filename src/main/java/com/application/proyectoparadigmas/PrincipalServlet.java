package com.application.proyectoparadigmas;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

@WebServlet(name = "principalServlet", value = "/principal")
public class PrincipalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String expresion = request.getParameter("expresion");
        ArrayList<EquationVariables> variableArray = new ArrayList<>();
        ArrayList<Object> equationArray = new ArrayList<>();

                expresion = expresion.replaceAll(" ", "");
                expresion = expresion.replaceAll("\\(","" );
                expresion = expresion.replaceAll("\\)","" );
                expresion = expresion.toLowerCase();
                int counter = 1;
                //loops through the equation and stores all characters between a and z in a variable array.
                for (int i = 0; i < expresion.length();i++){
                    if (expresion.charAt(i)>='a' && expresion.charAt(i)<='z') {
                        boolean alreadyExists = false;
                        EquationVariables temp = new EquationVariables(expresion.charAt(i),true, counter);

                        //checks for duplicate letters and doesn't add them to the array twice
                        for (EquationVariables v : variableArray){
                            if (v.getName()==temp.getName()){
                                alreadyExists = true;
                                temp = v;
                            }
                        }
                        if (!alreadyExists){
                            variableArray.add(temp);
                            //doubles the significant bit for each variable that is added
                            //First variable has 1, second has 2, third has 4, and so on
                            counter = counter*2;
                        }

                        //stores the variable objects that are created in an equation array as well
                        equationArray.add(temp);
                    }else{
                        //any non-letter characters get stored in an equation array
                        equationArray.add(expresion.charAt(i));
                    }
                }

                if (variableArray.size() > 0){
                    //Creates an instance of the truth table class with the proper parameters
                    TablaVerdad table = new TablaVerdad(variableArray, equationArray);
                    table.constructTable();

                    request.setAttribute("expresion", expresion);
                    request.getRequestDispatcher("index.html").forward(request, response);
                }else{
                    request.setAttribute("expresion", null);
                    request.getRequestDispatcher("index.html").forward(request, response);
                }
            }
}