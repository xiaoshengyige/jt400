///////////////////////////////////////////////////////////////////////////////
//                                                                             
// JTOpen (IBM Toolbox for Java - OSS version)                                 
//                                                                             
// Filename: JDSQLStatement.java
//                                                                             
// The source code contained herein is licensed under the IBM Public License   
// Version 1.0, which has been approved by the Open Source Initiative.         
// Copyright (C) 1997-2001 International Business Machines Corporation and     
// others. All rights reserved.                                                
//                                                                             
///////////////////////////////////////////////////////////////////////////////

package com.ibm.as400.access;

import java.sql.Connection;          // @G4A
import java.sql.SQLException;
import java.util.StringTokenizer;



/**
<p>This class represents a parsed SQL statement.
**/
//
// Implementation note:
//
// Originally, the statement was parsed as information
// was needed.  For example, it did not parse to see
// if "FOR UPDATE" appeared until isForUpdate() was
// called.  This strategy caused a lot of extraneous
// parsing, and most of the information is needed for
// most statements, so now all parsing is done at object
// construction time.
//
class JDSQLStatement
{
  private static final String copyright = "Copyright (C) 1997-2001 International Business Machines Corporation and others.";




    // Native statement types.
    //
    static final int    TYPE_UNDETERMINED  = 0;
    static final int    TYPE_OTHER         = 1;
    static final int    TYPE_SELECT        = 2;
    static final int    TYPE_CALL          = 3;
    static final int    TYPE_COMMIT        = 4;
    static final int    TYPE_ROLLBACK      = 5;
    static final int    TYPE_CONNECT       = 6;
    static final int    TYPE_BLOCK_INSERT  = 7;



    // String constants.  These will hopefully help performance
    // slightly - assuming a similar optimization does not
    // already take place.
    private static final String     AS_             = "AS";
    private static final String     CALL_           = "CALL";
    private static final String     CALL0_          = "?";                      // @E1A
    private static final String     CALL1_          = "?=";
    private static final String     CALL2_          = "?=CALL";
    private static final String     COMMA_          = ",";
    private static final String     CONNECT_        = "CONNECT";
    private static final String     CONNECTION_     = "CONNECTION";             // @F1A
    private static final String     CROSS_          = "CROSS";
    private static final String     CURRENT_        = "CURRENT";
    private static final String     DECLARE_        = "DECLARE";
    private static final String     DELETE_         = "DELETE";
    private static final String     DISCONNECT_     = "DISCONNECT";
    private static final String     EXCEPTION_      = "EXCEPTION";
    private static final String     FETCH_          = "FETCH";
    private static final String     FOR_            = "FOR";
    private static final String     FROM_           = "FROM";
    private static final String     INNER_          = "INNER";
    private static final String     INSERT_         = "INSERT";
    private static final String     JOIN_           = "JOIN";
    private static final String     LEFT_           = "LEFT";
    private static final String     LPAREN_         = "(";
    private static final String     OF_             = "OF";
    private static final String     ONLY_           = "ONLY";
    private static final String     READ_           = "READ";
    private static final String     RELEASE_        = "RELEASE";
    private static final String     ROWS_           = "ROWS";
    private static final String     SELECT_         = "SELECT";
    private static final String     SET_            = "SET";
    private static final String     UPDATE_         = "UPDATE";
    private static final String     VALUES_         = "VALUES";
    private static final String     WITH_           = "WITH";   // @B3A



    private String          correlationName_            = null;
    private String          csProcedure_                = null;     // @G4A
    private String          csSchema_                   = null;     // @G4A
    private boolean         hasReturnValueParameter_    = false;    // @E1A
    private boolean         isCall_                     = false;
    private boolean         isDeclare_                  = false;
    private boolean         isCurrentOf_                = false;
    private boolean         isDRDAConnect_              = false;    // @B1A
    private boolean         isDRDADisconnect_           = false;    // @B1A
    private boolean         isForFetchOrReadOnly_       = false;
    private boolean         isForUpdate_                = false;
    private boolean         isImmediatelyExecutable_    = false;
    private boolean         isInsert_                   = false;
    private boolean         isSelect_                   = false;
    private boolean         isSet_                      = false;    // @F4A
    private boolean         isSubSelect_                = false;
    private boolean         isPackaged_                 = false;
    private boolean         isUpdateOrDelete_           = false;
    private int             nativeType_                 = TYPE_OTHER;
    private int             numberOfParameters_;
    private String          selectTable_                = null;
    private StringTokenizer tokenizer_                  = null;
    private String          value_;
    private String          valueForServer_             = null;     // @E1A



    /**
    Constructs a JDSQLStatement object.  Use this constructor
    if you know you do not need to do any conversion to native
    SQL.
    
    @param  sql                 A SQL statement.
    
    @exception  SQLException        If there is a syntax error or
                                    a reference to an unsupported
                                    scalar function.
    **/
    JDSQLStatement(String sql) throws SQLException
    {
        // decimalSeparator is "" because we only need it if convert is true
        // this constructor is only used for SET TRANSACTION ISOLATION LEVEL called 
        // internally, so the last two parms can be null -- this code would need to 
        // be changed if this constructor were used for a CALL statement with named
        // parameters
        this(sql, "", false, JDProperties.PACKAGE_CRITERIA_DEFAULT, null);      // @A1C //@G1C
    }



    /**
    Constructs a JDSQLStatement object.
    
    @param  sql                 A SQL statement.
    @param  decimalSeparator    The decimal separator.
    @param  convert             Convert to native SQL?
    @param  packageCriteria     The package criteria.
    @param  connection          A connection object to get properties off.
    
    @exception  SQLException        If there is a syntax error or
                                    a reference to an unsupported
                                    scalar function.
    **/
    JDSQLStatement(String sql, String decimalSeparator, boolean convert, String packageCriteria,
                   Connection connection) // @A1C //@G1C
    throws SQLException
    {
        if (sql == null)
        {
            JDError.throwSQLException(JDError.EXC_SYNTAX_ERROR);
        }

        // Ensure that the string always contains at least one
        // character, since some methods depend on that fact.
        if (sql.trim().length() == 0)
        {
            JDError.throwSQLException(JDError.EXC_SYNTAX_BLANK);
        }

        //@F6D // Count the number of parameters.  Do not count parameter
        //@F6D // markers that appear within quotes or after a comment
        //@F6D // delimiter (two dashes).
        //@F6D numberOfParameters_ = 0;
        //@F6D int commentIndex = -1;
        //@F6D int length = sql.length();
        //@F6D for (int i = 0; i < length; ++i)
        //@F6D {
        //@F6D     char ch = sql.charAt(i);
        //@F6D     if (ch == '\'')
        //@F6D     {
        //@F6D         while ((i < length - 1) && (sql.charAt(++i) != '\''));
        //@F6D     }
        //@F6D     else if (ch == '\"')
        //@F6D     {
        //@F6D         while ((i < length - 1) && (sql.charAt (++i) != '\"'));
        //@F6D     }
        //@F6D     else if (ch == '-')
        //@F6D     {
        //@F6D         if (i < length - 1)
        //@F6D         {
        //@F6D             if (sql.charAt(++i) == '-')
        //@F6D             {
        //@F6D                 commentIndex = i;
        //@F6D                 i = length; // break out of for loop @F2C
        //@F6D             }
        //@F6D         }
        //@F6D     }
        //@F6D     else if (ch == '?')
        //@F6D     {
        //@F6D         ++numberOfParameters_;
        //@F6D     }
        //@F6D }


        //@F6A Start new code
        // Strip off comments.  Don't strip comment characters in literals.
        int length = sql.length ();

        char[] sqlArray = sql.toCharArray();
        char[] outArray = new char[length];
        int out = -1;  // We are always pre-incrementing... so start before the first char here.

        // Perf Note: numberOfParameters_ will default to 0.  Don't set it here.

        boolean inComment = false;

        for (int i = 0; i < length; ++i) {
            switch (sqlArray[i]) {
            case '\'':
                outArray[++out] = sqlArray[i];

                // Consume everything while looking for the ending quote character.
                while (i < length - 1) {
                    outArray[++out] = sqlArray[++i];
                    if (sqlArray[i] == '\'') {
                        break;
                    }
                }

                break;
            case '\"':
                outArray[++out] = sqlArray[i];

                // Consume everything while looking for the ending quote character.
                while (i < length - 1) {
                    outArray[++out] = sqlArray[++i];

                    if (sqlArray[i] == '\"') {
                        break;
                    }
                }

                break;
            case '-':
                if (i < length - 1) {
                    if (sqlArray[++i] == '-') {
                        // It's a single line commment (--).  We are going to eat the comment until
                        // a new line character or the end of the string, but first output a space 
                        // to the output buffer.
                        outArray[++out] = ' ';
                        while ((i < length - 1) && (sqlArray[++i] != '\n'))
                            ;                    // do nothing but spin.

                        // If we didn't break the loop because we were at the end of the string...
                        // we broke because of a newline character.  Put it into the output.
                        if (i < length - 1)
                            outArray[++out] = '\n';

                    }
                    else {
                        // This was not a comment.  Output the characters we read.
                        outArray[++out] = sqlArray[i-1];
                        outArray[++out] = sqlArray[i];
                    }
                }
                else {
                    // Last character - must write the '-'
                    outArray[++out] = sqlArray[i];
                }
                break;
            case '/':

                // If we are not on the last character....
                if (i < length - 1) {
                    // Check to see if we are starting a comment.
                    if (sqlArray[++i] == '*') {
                        // It is a multi-line commment - write over the '/*' characters
                        // and set the inComment flag.
                        outArray[++out] = ' ';
                        inComment = true;

                        // While we are not at the end....
                        while (i < length - 1) {
                            // if the next character is a '*' and there is at least one 
                            // character left after that, and that character is a '/' (don't 
                            // consume it immediately, we are ready to end the comment.
                            if ((sqlArray[++i] == '*') && (i < length - 1) && (sqlArray[i + 1] == '/')) {
                                inComment = false;
                                // You still have to eat the character that you looked ahead to.
                                ++i;
                                break;
                            }
                        }
                    }
                    else {
                        // This was not a comment.  Output the characters we read.
                        outArray[++out] = sqlArray[i-1];
                        outArray[++out] = sqlArray[i];
                    }
                }
                else {
                    // Last character - must write the '/'
                    outArray[++out] = sqlArray[i];
                }
                break;
            case '?':
                // Write the character.
                outArray[++out] = sqlArray[i];
                ++numberOfParameters_;
                break;
            default: 
                // Write the character.
                outArray[++out] = sqlArray[i];
                break;
            }
        }

        // Now turn it back into a String for processing.
        sql = new String(outArray, 0, ++out);
        //@F6A end new code

        // If we want to process escape syntax, then treat the
        // whole string as a big escape clause for parsing.
        if (convert)
        {
            //@F6D Weed off the comment before parsing.  This causes
            //@F6D problems if we try to handle it inside the parsing
            //@F6D code, since the parsing is recursive, but "skip-to-
            //@F6D the-end-of-the-line" is hard to implement in
            //@F6D recursive decent parsing.
            //@F6D Used to assume that as soon as we found a comment index that the rest of the 
            //@F6D string was invalid, but we can't assume that as customers put many comments
            //@F6D in their code now.
            //@F6D No longer have a commentIndex.  Code above takes care of this.
            //@F6D if (commentIndex >= 0)
            //@F6D {
            //@F6D    value_ = JDEscapeClause.parse(sql.substring(0, commentIndex), decimalSeparator) + sql.substring(commentIndex);
            //@F6D }
            //@F6D else
            //@F6D {
            value_ = JDEscapeClause.parse(sql, decimalSeparator);
            //@F6D }
        }
        else
        {
            value_ = sql;
        }



        // Determine the first word.
        // @F2 - We need to skip any leading parentheses and whitespace and combinations thereof.
        // e.g. SELECT
        //      (SELECT
        //      ( SELECT
        //      ((SELECT
        //      ( ( SELECT
        String firstWord = ""; //@F2C
        tokenizer_ = new StringTokenizer(value_);
        while (firstWord == "" && tokenizer_.hasMoreTokens()) //@F2A
        {
            String word = tokenizer_.nextToken();
            // Our StringTokenizer ensures that word.length() > 0
            int i=0;
            int len = word.length();
            while (i < len && word.charAt(i) == '(')
            {
                ++i;
            }
            if (i < len)
            {
                firstWord = word.substring(i).toUpperCase();
            }
        }
        //@F2D    if (tokenizer_.countTokens() > 0)
        //@F2D    {                                     // @E2C
        //@F2D      firstWord = tokenizer_.nextToken().toUpperCase();
        //@F2D      boolean flag = true;                                                // @E2A
        //@F2D      while (flag)
        //@F2D      {                                                       // @E2A
        //@F2D        if (firstWord.length() == 0)                                    // @E2A
        //@F2D        {
        //@F2D          flag = false;                                               // @E2A
        //@F2D        }
        //@F2D        else if (firstWord.charAt(0) == '(')                            // @E2A
        //@F2D        {
        //@F2D          firstWord = firstWord.substring(1);                         // @E2A
        //@F2D        }
        //@F2D        else                                                            // @E2A
        //@F2D        {
        //@F2D          flag = false;                                               // @E2A
        //@F2D        }
        //@F2D      }                                                                   // @E2A
        //@F2D    }                                                                       // @E2A
        //@F2D    else
        //@F2D    {
        //@F2D      firstWord = "";
        //@F2D    }

        // Handle the statement based on the first word
        if ((firstWord.startsWith(SELECT_)) || (firstWord.equals(WITH_))) // @F5C
        { // @B3C
            isSelect_ = true;
            nativeType_ = TYPE_SELECT;
        }
        else if ((firstWord.equals(CALL_)))
        {                                      // @E1A
            isCall_ = true;
            nativeType_ = TYPE_CALL;
        }
        else if ((firstWord.equals(CALL0_)) || (firstWord.equals(CALL1_)) || (firstWord.equals(CALL2_))) //@E1A
        {                                  // @E1A
            isCall_ = true;                                                         // @E1A
            nativeType_ = TYPE_CALL;                                                // @E1A
            hasReturnValueParameter_ = true;                                        // @E1A
            --numberOfParameters_;  // We will "fake" the first one.                // @E1A
        }                                                                           // @E1A
        else if (firstWord.equals(CONNECT_) || firstWord.equals(CONNECTION_) || firstWord.equals(DISCONNECT_) || firstWord.equals(RELEASE_)) //@F1C
        {
            nativeType_ = TYPE_CONNECT;

            if (firstWord.equals(CONNECT_))            // @B1A
            {
                isDRDAConnect_ = true;                  // @B1A
            }
            else if (firstWord.equals(DISCONNECT_))    // @B1A
            {
                isDRDADisconnect_ = true;               // @B1A
            }
        }
        else if (firstWord.equals(INSERT_))
        {
            isInsert_ = true;

            // Look for the string ROWS VALUES in the string.
            String upperCaseSql = value_.toUpperCase();
            int k = upperCaseSql.indexOf(ROWS_);
            if (k != -1)
            {
                int len = upperCaseSql.length(); //@F2A
                k += 4;
                while (k < len && Character.isWhitespace(upperCaseSql.charAt(k))) //@F2C
                {
                    ++k;
                }
                if (upperCaseSql.regionMatches(k, VALUES_, 0, 6))
                {
                    nativeType_ = TYPE_BLOCK_INSERT;
                }
            }
        }
        else if ((firstWord.equals(UPDATE_)) || (firstWord.equals(DELETE_)))
        {
            isUpdateOrDelete_ = true;
        }
        else if (firstWord.equals(DECLARE_))
        {
            isDeclare_ = true;
        }
        else if (firstWord.equals(SET_))  // @F4A - This entire block.
        {
            isSet_ = true;
            nativeType_ = TYPE_UNDETERMINED;
            // Note: See loop below for SET CONNECTION.
        }

        //@G4A New code starts
        if (isCall_)
        {
            // Strip off extra '?'s or '='s
            while (tokenizer_.hasMoreTokens() && !firstWord.endsWith(CALL_)) {
                firstWord = tokenizer_.nextToken ().toUpperCase ();
            }
            csSchema_ = tokenizer_.nextToken();

            int index = csSchema_.indexOf('(');
            // Strip off the beginning of the parameters.                                                       
            if (index != -1)
                csSchema_ = csSchema_.substring(0, index);

            String namingSeparator;
            if (((AS400JDBCConnection)connection).getProperties().
                getString(JDProperties.NAMING).equalsIgnoreCase("sql"))
            {
                namingSeparator = ".";
            }
            else
                namingSeparator = "/";
            index = csSchema_.indexOf(namingSeparator);
            if (index == -1) {
                csProcedure_ = csSchema_.toUpperCase();
                // Currently don't handle correctly if more than one library in list.
                csSchema_ = ((AS400JDBCConnection)connection).getProperties().
                            getString(JDProperties.LIBRARIES).toUpperCase();
            }
            else {
                csProcedure_ = csSchema_.substring(index+1).toUpperCase();
                csSchema_ = csSchema_.substring(0,index).toUpperCase();
            }
        }
        //@G4A New code ends

        // Now we need to do some parsing based on the
        // rest of the words.  These are tests for the
        // following certain phrases:
        //
        //    CURRENT OF
        //    FOR FETCH ONLY
        //    FOR READ ONLY
        //    FOR UPDATE
        //    FROM (select from-clause)
        //
        boolean isSecondToken = true;                   // @F4A
        while (tokenizer_.hasMoreTokens())
        {
            String token = tokenizer_.nextToken().toUpperCase();
            if (isInsert_ && token.equals(SELECT_))
            {
                isSubSelect_ = true;
            }
            else if (token.equals(CURRENT_) && tokenizer_.hasMoreTokens() &&
                     tokenizer_.nextToken().equalsIgnoreCase(OF_))
            {
                isCurrentOf_ = true;
            }
            else if (token.equals(FOR_))
            {
                parseFor();
            }
            else if (isSelect_ && token.equals(FROM_))
            {
                if (tokenizer_.hasMoreTokens())
                {
                    token = tokenizer_.nextToken(); //@F3C
                    if (!token.startsWith(LPAREN_))
                    {
                        selectTable_ = token;
                        if (tokenizer_.hasMoreTokens())
                        {
                            token = tokenizer_.nextToken().toUpperCase();

                            if (token.equals(AS_) && tokenizer_.hasMoreTokens())
                            {
                                correlationName_ = tokenizer_.nextToken().toUpperCase();
                            }
                            else if (token.equals(FOR_))
                            {
                                parseFor();
                            }
                        }
                    }
                }
            }
            else if (isSet_ && isSecondToken && token.equals(CONNECTION_))  // @F4A - This entire block.
            {
                nativeType_ = TYPE_CONNECT;
            }

            isSecondToken = false;      // @F4A
        }

        // Based on all of the information that we
        // have gathered up to this point, determine
        // a few more tidbits.
        boolean intermediate = (numberOfParameters_ > 0)
                               || (isInsert_ && isSubSelect_)
                               || (isCurrentOf_ && isUpdateOrDelete_);

        isImmediatelyExecutable_ = ! (intermediate || isSelect_);

        // @A1C
        // Changed the logic to determine isPackaged_ from the
        // "package criteria" property.
        if (packageCriteria.equalsIgnoreCase(JDProperties.PACKAGE_CRITERIA_DEFAULT))
        {  // @A1A
            isPackaged_ = intermediate
                          || (isSelect_ && ! isForFetchOrReadOnly_)
                          || (isDeclare_);
        }                                                                               // @A1A
        else
        {                                                                          // @A1A
            isPackaged_ = (isInsert_ && isSubSelect_)                                   // @A1A
                          || (isCurrentOf_ && isUpdateOrDelete_)                                  // @A1A
                          || (isSelect_ && ! isForFetchOrReadOnly_)                               // @A1A
                          || (isDeclare_);                                                        // @A1A
        }                                                                               // @A1A

        // If there is a return value parameter, strip if off now.                         @E1A
        // The server does not understand these.                                           @E1A
        if (hasReturnValueParameter_)
        {                                                 // @E1A
            int call = value_.toUpperCase().indexOf(CALL_);                             // @E1A
            if (call != -1)                                                             // @E1A
            {
                value_ = value_.substring(call);                                        // @E1A
            }
        }                                                                               // @E1A

        // Trim once and for all.                                                          @E1A
        value_ = value_.trim();                                                         // @E1A
    }



    /**
    Returns the number of parameters in the SQL statement.
    
    @return         Number of parameters.
    **/
    int countParameters()
    {
        return numberOfParameters_;
    }



    /**
    Returns the correlation name for a SELECT statement.
    
    @return The correlation name, or null if no correlation name
            is specified, or this is not a SELECT statement.
    **/
    String getCorrelationName()
    {
        return correlationName_;
    }



    /**
    Returns the native statement type.
    
    @return         Native type.
    **/
    int getNativeType()
    {
        return nativeType_;
    }



    //@G4A
    /**
    Returns the number of parameters for a statement.
    
    @return The number of parameters parsed in this class, or 0
            if no parameters were parsed.
    **/
    int getNumOfParameters()
    {
        return numberOfParameters_;
    }



    //@G4A
    /**
    Returns the procedure name name for a CALL stored procedure statement.
    
    @return The procedure name, or null if no stored procedure name
            is specified, or this is not a CALL statement.
    **/
    String getProcedure()
    {
        return csProcedure_;
    }



    //@G4A
    /**
    Returns the schema name for a CALL stored procedure statement.
    
    @return The schema name, or null if no stored procedure name
            is specified, or this is not a CALL statement.
    **/
    String getSchema()
    {
        return csSchema_;
    }



    /**
    Returns the single table name for a SELECT statement.
    
    @return The single table name, or null if multiple tables
            were specified, or this is not a SELECT statement.
    **/
    String getSelectTable()
    {
        return selectTable_;
    }



    // @E1A
    /**
    Indicates if the SQL statement has a return value parameter.
    
    @return true if the SQL statement has a return value parameter,
            false otherwise.
    **/
    boolean hasReturnValueParameter()                                   // @E1A
    {                                                                   // @E1A
        return hasReturnValueParameter_;                                // @E1A
    }                                                                   // @E1A



    // @B1A
    /**
    Indicates if the statement initiates a
    DRDA connection.
    
    @return     true if the statement initiates a
                DRDA connection; false otherwise.
    **/
    boolean isDRDAConnect()
    {
        return isDRDAConnect_;
    }



    // @B1A
    /**
    Indicates if the statement closes a
    DRDA connection.
    
    @return     true if the statement closes a
                DRDA connection; false otherwise.
    **/
    boolean isDRDADisconnect()
    {
        return isDRDADisconnect_;
    }



    /**
    Indicates if the statement contains a FOR FETCH
    ONLY or FOR READ ONLY clause.
    
    @return     true if the statement contains a
                FOR FETCH ONLY or FOR READ ONLY clause;
                false otherwise.
    **/
    boolean isForFetchOnly()
    {
        return isForFetchOrReadOnly_;
    }



    /**
    Indicates if the statement contains a FOR UPDATE
    clause.
    
    @return     true if the statement contains a
                FOR UPDATE clause; false otherwise.
    **/
    boolean isForUpdate()
    {
        return isForUpdate_;
    }



    /**
    Indicates if the statement can be executed immediately
    without doing a separate prepare and execute.
    
    @return     true if the statement can be executed
                immediately executable; false otherwise.
    **/
    boolean isImmediatelyExecutable()
    {
        return isImmediatelyExecutable_;
    }



    /**
    Indicates if this statement should be stored in
    a package.  This decision is based on characteristics
    that make statements good candidates for being
    stored in packages (those that will likely benefit
    overall performance by being stored in a package).
    This helps to reduce clutter in packages.
    
    @return     true if the statement should be stored
                in a package; false otherwise.
    **/
    boolean isPackaged()
    {
        return isPackaged_;
    }



    /**
    Indicates if the statement is a stored procedure call.
    
    @return     true if the statement is a stored
                procedure call; false otherwise.
    **/
    boolean isProcedureCall()
    {
        return isCall_;
    }



    /**
    Indicates if the statement a SELECT.
    
    @return     true if the statement is a SELECT;
                false otherwise.
    **/
    boolean isSelect()
    {
        return isSelect_;
    }



    /**
    Parses the token after FOR.
    **/
    private void parseFor()
    {
        if (tokenizer_.hasMoreTokens())
        {
            String token = tokenizer_.nextToken().toUpperCase();

            if ((token.equals(FETCH_)) || (token.equals(READ_)))
            {
                if (tokenizer_.hasMoreTokens() && tokenizer_.nextToken().equalsIgnoreCase(ONLY_))
                {
                    isForFetchOrReadOnly_ = true;
                }
            }
            else if (token.equals(UPDATE_))
            {
                isForUpdate_ = true;
            }
        }
    }


    /**
    Returns the SQL statement as a String.  This will be
    native SQL if conversion was requested.
    
    @return     The string, optionally native SQL.
    **/
    public String toString()
    {
        return value_;                                                     // @E1C
    }



}
