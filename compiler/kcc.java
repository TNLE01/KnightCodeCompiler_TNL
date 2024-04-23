/**
* Thisss Class Will Be Use To Get A Output File Made With ASM By The MyVisitor Class
* @author Truong Le
* @version 1.0
* Assignment 5
* CS322 - Compiler Construction
* Spring 2024
**/

package compiler;

import java.beans.Visibility;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.runtime.tree.TreeWizard.Visitor;
import org.antlr.v4.gui.Trees;

import lexparse.*;

public class kcc{
    
    public static void main(String[] args) {

        CharStream input;
        KnightCodeLexer lexer;
        CommonTokenStream tokens;
        KnightCodeParser parser;
         
        try{
            input = CharStreams.fromFileName(args[0]);  //get the input
            lexer = new KnightCodeLexer(input); //create the lexer
            tokens = new CommonTokenStream(lexer); //create the token stream
            parser = new KnightCodeParser(tokens); //create the parser
       
            ParseTree tree = parser.file();  //set the start location of the parser
             
            MyVisitor visitor;
            visitor = new MyVisitor(args[1]);

            visitor.visit(tree);

            visitor.end();
            
            visitor.ST.printTableAll();

        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }


    }



}
