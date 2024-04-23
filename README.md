# KnightCodeCompiler_TNL

This is the final project for CS322. The language is called KnightCode and it support only STRING and INTEGER as its data types.
Some other features include

    - Basic math (+, -, *, /) 
    - If statements
    - While Loops
    - Comparisons
    - Read input from user
    - Printing out Variables
    - #Comments

This is a basic program written with KnightCode:

    PROGRAM PWATF
    # Program With All The Feature
    DECLARE
        # Declaring Variables
        INTEGER w
        INTEGER x
	    INTEGER y
	    INTEGER z
        STRING word1
        STRING word2
        STRING word3
        STRING word4

    BEGIN
        # Program begins

        # Set Variables
        SET w := 5
        SET x := 10
        SET y := w + y
        SET word1 := "This is word1"
        SET word2 := "word2"
        SET word3 := "W3"

        # Read From User Input
        READ word4
        READ z

        # Print Variables
        PRINT w
        PRINT x
        PRINT y
        PRINT z
        PRINT word1
        PRINT word2
        PRINT word3
        PRINT word4

        # If Statement
        IF w <> 100 THEN PRINT "w not same as 100" ENDIF

        # If Statement With Else
        IF w = 100 THEN PRINT "w is the same as 100" ELSE PRINT "w not same as 100" ENDIF

        # While Loop
        WHILE y > 0 DO
		    PRINT y
		    SET y := y - 1
        ENDWHILE

    END

Output will look something like this:

    This is a user input, the same with the one number below
    10
    5
    10
    5
    10
    "This is word1"
    "word2"
    "W3"
    This is a user input, the same with the one number below
    "w not same as 100"
    "w not same as 100"
    5
    4
    3
    2
    1

To use, first you will need the required ASM and ANTLR packages installed on your system. Run the following commands in the command line to generate the grammar:

    ant build-grammar
    ant compile-grammar
    ant compile

You will then run the kcc.java file, along with two args. First is the file with the KnightCode and the second a location to generate the .class file:

    java compiler/kcc tests/program1.kc output/Program1

Run the new generated .class file after going into the output directory:

    java Program1

