/**
* WIll Walk The Parse Tree And Will Write The ASM Needed At Each Section
* @author Truong
* @version 1.0
* Assignment 5
* CS322 - Compiler Construction
* Spring 2024
**/



package compiler;



import lexparse.*;
import org.objectweb.asm.*;
import javax.print.DocFlavor.STRING;
import org.antlr.v4.runtime.*;



public class MyVisitor<T> extends KnightCodeBaseVisitor<T>{



    SymbolTable ST = new SymbolTable();
    int stackCounter = 2;
    String outputFileName;
    ClassWriter cw;
    MethodVisitor mv;



	/**
     * Constructor for the class
	 * @param outputFileName Name of output file
     */
    public MyVisitor(String outputFileName){this.outputFileName = outputFileName;}



	/**
     * Will write the ASM to a file using the Utilities file by Robert Kelley
     */
    public void end() {

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0,0);
        mv.visitEnd();

        cw.visitEnd();
        byte[] b = cw.toByteArray();
        Utilities.writeFile(b, outputFileName + ".class");
        System.out.println("Done!");

    }



	/**
	 * Will jump to a Label after comparing some numbers loaded onto the stack
	 * @param comp The symbol to use as a comparison
	 * @param ifTrueLabel The label to jump to if comparison equals true
	 */
	public void getCompare(String comp, Label ifTrueLabel) {
		if (comp.equals(">")) {mv.visitJumpInsn(Opcodes.IF_ICMPGT, ifTrueLabel);}
		else if (comp.equals("<")) {mv.visitJumpInsn(Opcodes.IF_ICMPLT, ifTrueLabel);}
		else if (comp.equals("=")) {mv.visitJumpInsn(Opcodes.IF_ICMPEQ, ifTrueLabel);}
		else if (comp.equals("<>")) {mv.visitJumpInsn(Opcodes.IF_ICMPNE, ifTrueLabel);}
	}



    /**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFile(KnightCodeParser.FileContext ctx) {
		// Write the Class file using ASM
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, ctx.getChild(1).getText(), null, "java/lang/Object",null);   
        {
			MethodVisitor mv=cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0); //load the first local variable: this
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V",false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(1,1);
			mv.visitEnd();
		}
        mv=cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
		// Import the Scanner Class
		mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
        mv.visitVarInsn(Opcodes.ASTORE, 1);

        return visitChildren(ctx);
    }



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDeclare(KnightCodeParser.DeclareContext ctx) {return visitChildren(ctx);}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariable(KnightCodeParser.VariableContext ctx) {
		// Get the data type and store it in the HashMap
        stackCounter ++;// System.out.println("visitVar");
		Variable var = new Variable<>(stackCounter, "Empty Type");
		ST.put(ctx.identifier().getText(), var);
		if (ctx.vartype().getText().equals("STRING")) {
			ST.get(ctx.identifier().getText()).setType("STRING");
			mv.visitLdcInsn((String)"Empty String Variable");
			mv.visitVarInsn(Opcodes.ASTORE, stackCounter);
		}
		else {
			ST.get(ctx.identifier().getText()).setType("INTEGER");
			mv.visitInsn(Opcodes.ICONST_0);
			mv.visitVarInsn(Opcodes.ISTORE, stackCounter);
		}
        ST.put(ctx.identifier().getText(), var);

        return visitChildren(ctx);
    }



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIdentifier(KnightCodeParser.IdentifierContext ctx) {return visitChildren(ctx);}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVartype(KnightCodeParser.VartypeContext ctx) {return visitChildren(ctx);}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBody(KnightCodeParser.BodyContext ctx) {return visitChildren(ctx);}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStat(KnightCodeParser.StatContext ctx) {return visitChildren(ctx);}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSetvar(KnightCodeParser.SetvarContext ctx) {
        // Assign Variable, Will go through the branch of the parse to get the data from each side
        //System.out.println("visitSetvar");
		if (ctx.getChild(3).getChildCount() == 0){
			mv.visitLdcInsn((String)ctx.getChild(3).getText());
            mv.visitVarInsn(Opcodes.ASTORE, ST.get(ctx.getChild(1).getText()).getStackLocation());
		}
		else {
			visit(ctx.getChild(3));
			mv.visitVarInsn(Opcodes.ISTORE, ST.get(ctx.getChild(1).getText()).getStackLocation());
		}

        return null;
    }



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParenthesis(KnightCodeParser.ParenthesisContext ctx) {
		visit(ctx.getChild(1));
		return null;
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitMultiplication(KnightCodeParser.MultiplicationContext ctx) {
		visit(ctx.getChild(0));
        visit(ctx.getChild(2));
        mv.visitInsn(Opcodes.IMUL);
		return null;
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitAddition(KnightCodeParser.AdditionContext ctx) {
		visit(ctx.getChild(0));
        visit(ctx.getChild(2));
        mv.visitInsn(Opcodes.IADD);
        return null;
    }



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSubtraction(KnightCodeParser.SubtractionContext ctx) {
		visit(ctx.getChild(0));
        visit(ctx.getChild(2));
        mv.visitInsn(Opcodes.ISUB);
		return null;
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitNumber(KnightCodeParser.NumberContext ctx) {
		mv.visitLdcInsn(Integer.valueOf(ctx.getText()));
		return visitChildren(ctx);
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitComparison(KnightCodeParser.ComparisonContext ctx) {
		// Uses Labels to jump if true
		Label ifTrueComp = new Label();
        Label endComp = new Label();

		visit(ctx.getChild(0));
        visit(ctx.getChild(2));
		getCompare(ctx.getChild(1).getText(), ifTrueComp);

		mv.visitLdcInsn(0);
		mv.visitJumpInsn(Opcodes.GOTO, endComp);
		mv.visitLabel(ifTrueComp);
		mv.visitLdcInsn(1);
		mv.visitLabel(endComp);

		return null;
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDivision(KnightCodeParser.DivisionContext ctx) {
		visit(ctx.getChild(0));
        visit(ctx.getChild(2));
        mv.visitInsn(Opcodes.IDIV);
		return null;
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitId(KnightCodeParser.IdContext ctx) {
        mv.visitVarInsn(Opcodes.ILOAD, ST.get(ctx.getText()).getStackLocation());
		return visitChildren(ctx);
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitComp(KnightCodeParser.CompContext ctx) {return visitChildren(ctx);}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPrint(KnightCodeParser.PrintContext ctx) {
        // Get the data type of the Variable and print it using the correct ASM code
        String child = ctx.getChild(1).getText();
        if (ST.containsKey(child)) { 			
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			if (ST.get(child).getType().equals("STRING")){
				mv.visitVarInsn(Opcodes.ALOAD, ST.get(child).getStackLocation());
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			}
			else {
				mv.visitVarInsn(Opcodes.ILOAD, ST.get(child).getStackLocation());
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
			}
        }
        else {
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(child);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        return visitChildren(ctx);
    }



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitRead(KnightCodeParser.ReadContext ctx) {
		// Get the data type in order to read the input correctly and store it using the correct ASM code
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		if (ST.get(ctx.ID().getText()).getType().equals("STRING")) {
        	mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
        	mv.visitVarInsn(Opcodes.ASTORE, ST.get(ctx.ID().getText()).getStackLocation());
		}
		else {
        	mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
        	mv.visitVarInsn(Opcodes.ISTORE, ST.get(ctx.ID().getText()).getStackLocation());
		}

		return visitChildren(ctx);
	}



	/**
	 * 
	 */
	public void getData(String variable) {
		if (ST.containsKey(variable) == true) {mv.visitVarInsn(Opcodes.ILOAD, ST.get(variable).getStackLocation());}
		else {mv.visitLdcInsn(Integer.valueOf(variable));}}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDecision(KnightCodeParser.DecisionContext ctx) {
		// Uses a for loop to see if an ELSE exist, uses the labels to jump if true and uses the correct loop based on if an ELSE exist
		Label ifTrue = new Label();
    	Label end = new Label();

		boolean ifElse = false;
		int ifThan = 1;
		for (int i = 5; i < ctx.getChildCount(); i++) {if (ctx.getChild(i).getText().equals("ELSE")){ifElse = true; ifThan = i;}}

		getData(ctx.getChild(1).getText());
		getData(ctx.getChild(3).getText());
		getCompare(ctx.comp().getText(), ifTrue);
		
		if (ifElse == true) {for (int i = ifThan; i < ctx.getChildCount(); i++) {visit(ctx.getChild(i));}}
		mv.visitJumpInsn(Opcodes.GOTO, end);
		
		mv.visitLabel(ifTrue);
		if (ifElse == true) {for (int i = 5; i < ifThan; i++) {visit(ctx.getChild(i));}}
		else {for (int i = 5; i < ctx.getChildCount(); i++) {visit(ctx.getChild(i));}}

		mv.visitLabel(end);
		return null;
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLoop(KnightCodeParser.LoopContext ctx) {
		// Switches up the comparison ASM code as well as the jump label to jump back up to the beginning if true and down at the bottom if false
		Label ifTrueLoop = new Label();
        Label endLoop = new Label();

		mv.visitLabel(ifTrueLoop);

		getData(ctx.getChild(1).getText());
		getData(ctx.getChild(3).getText());

		if (ctx.getChild(2).getText().equals(">")) {mv.visitJumpInsn(Opcodes.IF_ICMPLE, endLoop);}
		else if (ctx.getChild(2).getText().equals("<")) {mv.visitJumpInsn(Opcodes.IF_ICMPGT, endLoop);}
		else if (ctx.getChild(2).getText().equals("=")) {mv.visitJumpInsn(Opcodes.IF_ICMPNE, endLoop);}
		else if (ctx.getChild(2).getText().equals("<>")) {mv.visitJumpInsn(Opcodes.IF_ICMPEQ, endLoop);}

        for(int i = 5 ; i < ctx.getChildCount(); i++) {visit(ctx.getChild(i));}
		mv.visitJumpInsn(Opcodes.GOTO, ifTrueLoop);

    	mv.visitLabel(endLoop);
		return null;
	}



}
