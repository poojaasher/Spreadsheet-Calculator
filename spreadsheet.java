import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/* I have created an Expressions class that stores the expression and parses and calculates expression values. I have a flag that breaks when cyclic dependency is found. I have also coded with the extra credits in mind. The main function takes inputs, calls the function to process them and prints the results. Expressions contains two functions for plain numeric expressions and expressions with references. */

public class Solution {
    static Expression[][] cells;
    int max;
    static boolean breaker=false;
    
    Solution(int m,int n){
        cells=new Expression[m][n];
        max=m*n;
    }
    
    public class Expression{
        int numcycle;
        String exp;
        double value;
        Expression data;
        // 1:Numerical Expression 2:Reference Expression
        int type;
        Expression(String op){
            this.exp=op;
            //this.typeEvaluator();
            this.value=-1;
            numcycle=0;
        }
        public void typeEvaluator(){
            if(!breaker){
                String regex=".*[A-Z].*";
                this.type=1;
                if(this.exp.matches(regex)){
                    this.type=2;
                    this.EvaluateRef();
                }
                else{
                    this.EvaluateNum();
                    //System.out.println(this.value);
                }
            }
        }
        public void printValue(){
            System.out.println(String.format( "%.5f", this.value));
        }
        public void printExp(){
            System.out.print(this.exp);
        }
        // Process numerical expressions (eg. 4 5 *)
        public void EvaluateNum(){
            Stack<Double> st=new Stack<Double>();
            String[] digits=exp.split(" ");
            double current=0;
            for(int i=0;i<digits.length;i++){
                try{
                    double num=Double.parseDouble(digits[i]);
                    st.push(num);
                }
                catch(NumberFormatException nfe){
                    if(digits[i].equals("--")){
                        double a=st.pop();
                        st.push(--a);
                    }
                    else if(digits[i].equals("++")){
                        double a=st.pop();
                        st.push(++a);
                    }
                    else {
                        double a=st.pop();
                        double b=st.pop();
                        //System.out.println(a+"\t"+b+"\t"+digits[i]);
                        if(digits[i].equals("*")){
                            current=b*a;
                        }
                        else if(digits[i].equals("/")){
                            current=b/a;
                        }
                        else if(digits[i].equals("+")){
                            current=b+a;
                        }
                        else if(digits[i].equals("-")){
                            current=b-a;
                        }
                        st.push(current);
                        //System.out.println(current);
                    }
                }
            }
            //System.out.println(st.peek());
            this.value=st.pop();
        }
        // Process expressions with cell references (eg. A1 A2 +)
        public void EvaluateRef(){
            Stack<Double> st=new Stack<Double>();
            String[] digits=exp.split(" ");
            if(digits.length==1){
                int row=digits[0].charAt(0)-65;
                int col=Integer.parseInt(digits[0].substring(1,digits[0].length()))-1;
                //System.out.println(col);
                this.value=cells[row][col].getValue(numcycle+1);
                return;
            }
            double current=0;
            for(int i=0;i<digits.length;i++){
                if(breaker)
                    break;
                try{
                    double num=Double.parseDouble(digits[i]);
                    st.push(num);
                }
                catch(NumberFormatException nfe){
                    String regex=".*[A-Z].*";
                    if(digits[i].matches(regex)){
                        int row=digits[i].charAt(0)-65;
                        int col=Integer.parseInt(digits[i].substring(1,digits[i].length()))-1;
                        
                        double x=cells[row][col].getValue(numcycle+1);
                        st.push(x);
                    }
                    else{
                        double a=st.pop();
                        double b=st.pop();
                        if(digits[i].equals("*")){
                            current=b*a;
                        }
                        else if(digits[i].equals("/")){
                            current=b/a;
                        }
                        else if(digits[i].equals("+")){
                            current=b+a;
                        }
                        else if(digits[i].equals("-")){
                            current=b-a;
                        }
                        st.push(current);
                    }
                }
            }
            this.value=st.pop();
        }
        public double getValue(int cycle){
            if(this.value!=-1)
                return this.value;
            else{
                this.numcycle=cycle;
                if(cycle<=max){
                    this.typeEvaluator();
                    return this.value;
                }
                else{
                    breaker=true;
                }
            }
            return -2;
        }
        
    }
    
    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        sc.nextLine();
        Solution sol=new Solution(m,n);
        // Storing inputs
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                cells[i][j]=sol.new Expression(sc.nextLine());
            }
        }
        // Processing expressions
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(breaker)
                    break;
                cells[i][j].typeEvaluator();
            }
        }
        // Printing the results. If breaker is set, it indicates a circular dependency.
        if(!breaker){
            for(int i=0;i<m;i++){
                for(int j=0;j<n;j++){
                    cells[i][j].printValue();
                }
            }
        }
        else{
            System.out.println("Error: Circular dependency!");
        }
    }
}