// Generated from C:/Users/sunshine/Documents/Complier/src/Parser\Mx.g4 by ANTLR 4.9
package Parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, BoolLiteral=33, IntLiteral=34, StringLiteral=35, ESC=36, IDENTIFIER=37, 
		INT=38, BOOL=39, STRING=40, NULL=41, VOID=42, TRUE=43, FALSE=44, IF=45, 
		ELSE=46, FOR=47, WHILE=48, BREAK=49, CONTINUE=50, RETURN=51, NEW=52, CLASS=53, 
		THIS=54, Whitespace=55, Newline=56, LineComment=57;
	public static final int
		RULE_program = 0, RULE_funcDef = 1, RULE_classDef = 2, RULE_varDef = 3, 
		RULE_varDefList = 4, RULE_varDefUnit = 5, RULE_constructorDef = 6, RULE_type = 7, 
		RULE_primitiveType = 8, RULE_nonArrayType = 9, RULE_paramList = 10, RULE_param = 11, 
		RULE_block = 12, RULE_statement = 13, RULE_expr = 14, RULE_exprList = 15, 
		RULE_newInstance = 16, RULE_constant = 17;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "funcDef", "classDef", "varDef", "varDefList", "varDefUnit", 
			"constructorDef", "type", "primitiveType", "nonArrayType", "paramList", 
			"param", "block", "statement", "expr", "exprList", "newInstance", "constant"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "')'", "'{'", "'}'", "','", "'='", "'['", "']'", 
			"'++'", "'--'", "'.'", "'+'", "'-'", "'!'", "'~'", "'*'", "'/'", "'%'", 
			"'<<'", "'>>'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&'", 
			"'|'", "'^'", "'&&'", "'||'", null, null, null, null, null, "'int'", 
			"'bool'", "'string'", "'null'", "'void'", "'true'", "'false'", "'if'", 
			"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", 
			"'class'", "'this'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "BoolLiteral", 
			"IntLiteral", "StringLiteral", "ESC", "IDENTIFIER", "INT", "BOOL", "STRING", 
			"NULL", "VOID", "TRUE", "FALSE", "IF", "ELSE", "FOR", "WHILE", "BREAK", 
			"CONTINUE", "RETURN", "NEW", "CLASS", "THIS", "Whitespace", "Newline", 
			"LineComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public List<FuncDefContext> funcDef() {
			return getRuleContexts(FuncDefContext.class);
		}
		public FuncDefContext funcDef(int i) {
			return getRuleContext(FuncDefContext.class,i);
		}
		public List<ClassDefContext> classDef() {
			return getRuleContexts(ClassDefContext.class);
		}
		public ClassDefContext classDef(int i) {
			return getRuleContext(ClassDefContext.class,i);
		}
		public List<VarDefContext> varDef() {
			return getRuleContexts(VarDefContext.class);
		}
		public VarDefContext varDef(int i) {
			return getRuleContext(VarDefContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << IDENTIFIER) | (1L << INT) | (1L << BOOL) | (1L << STRING) | (1L << VOID) | (1L << CLASS))) != 0)) {
				{
				setState(40);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(36);
					funcDef();
					}
					break;
				case 2:
					{
					setState(37);
					classDef();
					}
					break;
				case 3:
					{
					setState(38);
					varDef();
					}
					break;
				case 4:
					{
					setState(39);
					match(T__0);
					}
					break;
				}
				}
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncDefContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode VOID() { return getToken(MxParser.VOID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FuncDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterFuncDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitFuncDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFuncDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDefContext funcDef() throws RecognitionException {
		FuncDefContext _localctx = new FuncDefContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_funcDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VOID:
				{
				setState(45);
				match(VOID);
				}
				break;
			case IDENTIFIER:
			case INT:
			case BOOL:
			case STRING:
				{
				setState(46);
				type(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(49);
			match(IDENTIFIER);
			setState(50);
			match(T__1);
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IDENTIFIER) | (1L << INT) | (1L << BOOL) | (1L << STRING))) != 0)) {
				{
				setState(51);
				paramList();
				}
			}

			setState(54);
			match(T__2);
			setState(55);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDefContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(MxParser.CLASS, 0); }
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public List<VarDefContext> varDef() {
			return getRuleContexts(VarDefContext.class);
		}
		public VarDefContext varDef(int i) {
			return getRuleContext(VarDefContext.class,i);
		}
		public List<FuncDefContext> funcDef() {
			return getRuleContexts(FuncDefContext.class);
		}
		public FuncDefContext funcDef(int i) {
			return getRuleContext(FuncDefContext.class,i);
		}
		public List<ConstructorDefContext> constructorDef() {
			return getRuleContexts(ConstructorDefContext.class);
		}
		public ConstructorDefContext constructorDef(int i) {
			return getRuleContext(ConstructorDefContext.class,i);
		}
		public ClassDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterClassDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitClassDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDefContext classDef() throws RecognitionException {
		ClassDefContext _localctx = new ClassDefContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			match(CLASS);
			setState(58);
			match(IDENTIFIER);
			setState(59);
			match(T__3);
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IDENTIFIER) | (1L << INT) | (1L << BOOL) | (1L << STRING) | (1L << VOID))) != 0)) {
				{
				setState(63);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(60);
					varDef();
					}
					break;
				case 2:
					{
					setState(61);
					funcDef();
					}
					break;
				case 3:
					{
					setState(62);
					constructorDef();
					}
					break;
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
			match(T__4);
			setState(69);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDefContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VarDefListContext varDefList() {
			return getRuleContext(VarDefListContext.class,0);
		}
		public VarDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterVarDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitVarDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDefContext varDef() throws RecognitionException {
		VarDefContext _localctx = new VarDefContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_varDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			type(0);
			setState(72);
			varDefList();
			setState(73);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDefListContext extends ParserRuleContext {
		public List<VarDefUnitContext> varDefUnit() {
			return getRuleContexts(VarDefUnitContext.class);
		}
		public VarDefUnitContext varDefUnit(int i) {
			return getRuleContext(VarDefUnitContext.class,i);
		}
		public VarDefListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDefList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterVarDefList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitVarDefList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarDefList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDefListContext varDefList() throws RecognitionException {
		VarDefListContext _localctx = new VarDefListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_varDefList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			varDefUnit();
			setState(80);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(76);
				match(T__5);
				setState(77);
				varDefUnit();
				}
				}
				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDefUnitContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VarDefUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDefUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterVarDefUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitVarDefUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarDefUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDefUnitContext varDefUnit() throws RecognitionException {
		VarDefUnitContext _localctx = new VarDefUnitContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_varDefUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			match(IDENTIFIER);
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(84);
				match(T__6);
				setState(85);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstructorDefContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public ConstructorDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterConstructorDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitConstructorDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstructorDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorDefContext constructorDef() throws RecognitionException {
		ConstructorDefContext _localctx = new ConstructorDefContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_constructorDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(IDENTIFIER);
			setState(89);
			match(T__1);
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IDENTIFIER) | (1L << INT) | (1L << BOOL) | (1L << STRING))) != 0)) {
				{
				setState(90);
				paramList();
				}
			}

			setState(93);
			match(T__2);
			setState(94);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(97);
			nonArrayType();
			}
			_ctx.stop = _input.LT(-1);
			setState(104);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TypeContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(99);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(100);
					match(T__7);
					setState(101);
					match(T__8);
					}
					} 
				}
				setState(106);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode BOOL() { return getToken(MxParser.BOOL, 0); }
		public TerminalNode INT() { return getToken(MxParser.INT, 0); }
		public TerminalNode STRING() { return getToken(MxParser.STRING, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitPrimitiveType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << BOOL) | (1L << STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonArrayTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public NonArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonArrayType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterNonArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitNonArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNonArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonArrayTypeContext nonArrayType() throws RecognitionException {
		NonArrayTypeContext _localctx = new NonArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_nonArrayType);
		try {
			setState(111);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case BOOL:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(109);
				primitiveType();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(110);
				match(IDENTIFIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitParamList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			param();
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(114);
				match(T__5);
				setState(115);
				param();
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			type(0);
			setState(122);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(T__3);
			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__3) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << IDENTIFIER) | (1L << INT) | (1L << BOOL) | (1L << STRING) | (1L << NULL) | (1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << BREAK) | (1L << CONTINUE) | (1L << RETURN) | (1L << NEW) | (1L << THIS))) != 0)) {
				{
				{
				setState(125);
				statement();
				}
				}
				setState(130);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(131);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class VarDefStmtContext extends StatementContext {
		public VarDefContext varDef() {
			return getRuleContext(VarDefContext.class,0);
		}
		public VarDefStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterVarDefStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitVarDefStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVarDefStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForStmtContext extends StatementContext {
		public ExprContext init;
		public ExprContext cond;
		public ExprContext step;
		public TerminalNode FOR() { return getToken(MxParser.FOR, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterForStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitForStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprStmtContext extends StatementContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterExprStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitExprStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExprStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileStmtContext extends StatementContext {
		public TerminalNode WHILE() { return getToken(MxParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterWhileStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitWhileStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfStmtContext extends StatementContext {
		public TerminalNode IF() { return getToken(MxParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MxParser.ELSE, 0); }
		public IfStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BlockStmtContext extends StatementContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BlockStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBlockStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBlockStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BreakStmtContext extends StatementContext {
		public TerminalNode BREAK() { return getToken(MxParser.BREAK, 0); }
		public BreakStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBreakStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBreakStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EmptyStmtContext extends StatementContext {
		public EmptyStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterEmptyStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitEmptyStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitEmptyStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnStmtContext extends StatementContext {
		public TerminalNode RETURN() { return getToken(MxParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterReturnStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitReturnStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueStmtContext extends StatementContext {
		public TerminalNode CONTINUE() { return getToken(MxParser.CONTINUE, 0); }
		public ContinueStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterContinueStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitContinueStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_statement);
		int _la;
		try {
			setState(178);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(133);
				block();
				}
				break;
			case 2:
				_localctx = new VarDefStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(134);
				varDef();
				}
				break;
			case 3:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(135);
				match(IF);
				setState(136);
				match(T__1);
				setState(137);
				expr(0);
				setState(138);
				match(T__2);
				setState(139);
				statement();
				setState(142);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(140);
					match(ELSE);
					setState(141);
					statement();
					}
					break;
				}
				}
				break;
			case 4:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(144);
				match(FOR);
				setState(145);
				match(T__1);
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << IDENTIFIER) | (1L << NULL) | (1L << NEW) | (1L << THIS))) != 0)) {
					{
					setState(146);
					((ForStmtContext)_localctx).init = expr(0);
					}
				}

				setState(149);
				match(T__0);
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << IDENTIFIER) | (1L << NULL) | (1L << NEW) | (1L << THIS))) != 0)) {
					{
					setState(150);
					((ForStmtContext)_localctx).cond = expr(0);
					}
				}

				setState(153);
				match(T__0);
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << IDENTIFIER) | (1L << NULL) | (1L << NEW) | (1L << THIS))) != 0)) {
					{
					setState(154);
					((ForStmtContext)_localctx).step = expr(0);
					}
				}

				setState(157);
				match(T__2);
				setState(158);
				statement();
				}
				break;
			case 5:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(159);
				match(WHILE);
				setState(160);
				match(T__1);
				setState(161);
				expr(0);
				setState(162);
				match(T__2);
				setState(163);
				statement();
				}
				break;
			case 6:
				_localctx = new BreakStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(165);
				match(BREAK);
				setState(166);
				match(T__0);
				}
				break;
			case 7:
				_localctx = new ContinueStmtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(167);
				match(CONTINUE);
				setState(168);
				match(T__0);
				}
				break;
			case 8:
				_localctx = new ReturnStmtContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(169);
				match(RETURN);
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << IDENTIFIER) | (1L << NULL) | (1L << NEW) | (1L << THIS))) != 0)) {
					{
					setState(170);
					expr(0);
					}
				}

				setState(173);
				match(T__0);
				}
				break;
			case 9:
				_localctx = new ExprStmtContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(174);
				expr(0);
				setState(175);
				match(T__0);
				}
				break;
			case 10:
				_localctx = new EmptyStmtContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(177);
				match(T__0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NewExprContext extends ExprContext {
		public TerminalNode NEW() { return getToken(MxParser.NEW, 0); }
		public NewInstanceContext newInstance() {
			return getRuleContext(NewInstanceContext.class,0);
		}
		public NewExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterNewExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitNewExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNewExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrefixExprContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PrefixExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterPrefixExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitPrefixExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrefixExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ThisExprContext extends ExprContext {
		public TerminalNode THIS() { return getToken(MxParser.THIS, 0); }
		public ThisExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterThisExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitThisExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitThisExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubscriptExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public SubscriptExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterSubscriptExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitSubscriptExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSubscriptExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MemberExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public MemberExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterMemberExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitMemberExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitMemberExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryExprContext extends ExprContext {
		public ExprContext opd1;
		public Token op;
		public ExprContext opd2;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public BinaryExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterBinaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitBinaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PostfixExprContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PostfixExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterPostfixExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitPostfixExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPostfixExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FuncCallExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public FuncCallExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterFuncCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitFuncCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFuncCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SubExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterSubExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitSubExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSubExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConstExprContext extends ExprContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ConstExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterConstExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitConstExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdExprContext extends ExprContext {
		public TerminalNode IDENTIFIER() { return getToken(MxParser.IDENTIFIER, 0); }
		public IdExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterIdExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitIdExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIdExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NEW:
				{
				_localctx = new NewExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(181);
				match(NEW);
				setState(182);
				newInstance();
				}
				break;
			case T__9:
			case T__10:
				{
				_localctx = new PrefixExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(183);
				((PrefixExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
					((PrefixExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(184);
				expr(18);
				}
				break;
			case T__12:
			case T__13:
				{
				_localctx = new PrefixExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(185);
				((PrefixExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
					((PrefixExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(186);
				expr(17);
				}
				break;
			case T__14:
			case T__15:
				{
				_localctx = new PrefixExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(187);
				((PrefixExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__14 || _la==T__15) ) {
					((PrefixExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(188);
				expr(16);
				}
				break;
			case T__1:
				{
				_localctx = new SubExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189);
				match(T__1);
				setState(190);
				expr(0);
				setState(191);
				match(T__2);
				}
				break;
			case THIS:
				{
				_localctx = new ThisExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(193);
				match(THIS);
				}
				break;
			case BoolLiteral:
			case IntLiteral:
			case StringLiteral:
			case NULL:
				{
				_localctx = new ConstExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194);
				constant();
				}
				break;
			case IDENTIFIER:
				{
				_localctx = new IdExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(195);
				match(IDENTIFIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(249);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(247);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(198);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(199);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__16) | (1L << T__17) | (1L << T__18))) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(200);
						((BinaryExprContext)_localctx).opd2 = expr(16);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(201);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(202);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(203);
						((BinaryExprContext)_localctx).opd2 = expr(15);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(204);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(205);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__19 || _la==T__20) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(206);
						((BinaryExprContext)_localctx).opd2 = expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(207);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(208);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24))) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(209);
						((BinaryExprContext)_localctx).opd2 = expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(210);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(211);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__25 || _la==T__26) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(212);
						((BinaryExprContext)_localctx).opd2 = expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(213);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(214);
						((BinaryExprContext)_localctx).op = match(T__27);
						setState(215);
						((BinaryExprContext)_localctx).opd2 = expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(216);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(217);
						((BinaryExprContext)_localctx).op = match(T__28);
						setState(218);
						((BinaryExprContext)_localctx).opd2 = expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(219);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(220);
						((BinaryExprContext)_localctx).op = match(T__29);
						setState(221);
						((BinaryExprContext)_localctx).opd2 = expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(222);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(223);
						((BinaryExprContext)_localctx).op = match(T__30);
						setState(224);
						((BinaryExprContext)_localctx).opd2 = expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(225);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(226);
						((BinaryExprContext)_localctx).op = match(T__31);
						setState(227);
						((BinaryExprContext)_localctx).opd2 = expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).opd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(228);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(229);
						((BinaryExprContext)_localctx).op = match(T__6);
						setState(230);
						((BinaryExprContext)_localctx).opd2 = expr(5);
						}
						break;
					case 12:
						{
						_localctx = new PostfixExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(231);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(232);
						((PostfixExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__10) ) {
							((PostfixExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 13:
						{
						_localctx = new MemberExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(233);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(234);
						match(T__11);
						setState(235);
						match(IDENTIFIER);
						}
						break;
					case 14:
						{
						_localctx = new FuncCallExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(236);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(237);
						match(T__1);
						setState(239);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__9) | (1L << T__10) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << IDENTIFIER) | (1L << NULL) | (1L << NEW) | (1L << THIS))) != 0)) {
							{
							setState(238);
							exprList();
							}
						}

						setState(241);
						match(T__2);
						}
						break;
					case 15:
						{
						_localctx = new SubscriptExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(242);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(243);
						match(T__7);
						setState(244);
						expr(0);
						setState(245);
						match(T__8);
						}
						break;
					}
					} 
				}
				setState(251);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExprListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			expr(0);
			setState(257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__5) {
				{
				{
				setState(253);
				match(T__5);
				setState(254);
				expr(0);
				}
				}
				setState(259);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NewInstanceContext extends ParserRuleContext {
		public NewInstanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newInstance; }
	 
		public NewInstanceContext() { }
		public void copyFrom(NewInstanceContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayNewContext extends NewInstanceContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArrayNewContext(NewInstanceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterArrayNew(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitArrayNew(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayNew(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WrongNewContext extends NewInstanceContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public WrongNewContext(NewInstanceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterWrongNew(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitWrongNew(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWrongNew(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ClassNewContext extends NewInstanceContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public ClassNewContext(NewInstanceContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterClassNew(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitClassNew(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassNew(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewInstanceContext newInstance() throws RecognitionException {
		NewInstanceContext _localctx = new NewInstanceContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_newInstance);
		try {
			int _alt;
			setState(305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				_localctx = new WrongNewContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(260);
				nonArrayType();
				setState(267);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(261);
						match(T__7);
						setState(262);
						expr(0);
						setState(263);
						match(T__8);
						}
						} 
					}
					setState(269);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				}
				setState(272); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(270);
						match(T__7);
						setState(271);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(274); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(280); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(276);
						match(T__7);
						setState(277);
						expr(0);
						setState(278);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(282); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				_localctx = new ArrayNewContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(284);
				nonArrayType();
				setState(289); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(285);
						match(T__7);
						setState(286);
						expr(0);
						setState(287);
						match(T__8);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(291); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(297);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(293);
						match(T__7);
						setState(294);
						match(T__8);
						}
						} 
					}
					setState(299);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				}
				break;
			case 3:
				_localctx = new ClassNewContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(300);
				nonArrayType();
				setState(301);
				match(T__1);
				setState(302);
				match(T__2);
				}
				break;
			case 4:
				_localctx = new ClassNewContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(304);
				nonArrayType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode BoolLiteral() { return getToken(MxParser.BoolLiteral, 0); }
		public TerminalNode IntLiteral() { return getToken(MxParser.IntLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(MxParser.StringLiteral, 0); }
		public TerminalNode NULL() { return getToken(MxParser.NULL, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxListener ) ((MxListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << NULL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 7:
			return type_sempred((TypeContext)_localctx, predIndex);
		case 14:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 15);
		case 2:
			return precpred(_ctx, 14);
		case 3:
			return precpred(_ctx, 13);
		case 4:
			return precpred(_ctx, 12);
		case 5:
			return precpred(_ctx, 11);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 9);
		case 8:
			return precpred(_ctx, 8);
		case 9:
			return precpred(_ctx, 7);
		case 10:
			return precpred(_ctx, 6);
		case 11:
			return precpred(_ctx, 5);
		case 12:
			return precpred(_ctx, 23);
		case 13:
			return precpred(_ctx, 21);
		case 14:
			return precpred(_ctx, 20);
		case 15:
			return precpred(_ctx, 19);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3;\u0138\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\3\2\3\2\3\2\3\2\7\2+\n\2\f\2\16\2.\13\2\3\3\3\3\5\3\62\n\3"+
		"\3\3\3\3\3\3\5\3\67\n\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4B\n\4\f"+
		"\4\16\4E\13\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\7\6Q\n\6\f\6\16"+
		"\6T\13\6\3\7\3\7\3\7\5\7Y\n\7\3\b\3\b\3\b\5\b^\n\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\7\ti\n\t\f\t\16\tl\13\t\3\n\3\n\3\13\3\13\5\13r\n\13"+
		"\3\f\3\f\3\f\7\fw\n\f\f\f\16\fz\13\f\3\r\3\r\3\r\3\16\3\16\7\16\u0081"+
		"\n\16\f\16\16\16\u0084\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\5\17\u0091\n\17\3\17\3\17\3\17\5\17\u0096\n\17\3\17\3\17"+
		"\5\17\u009a\n\17\3\17\3\17\5\17\u009e\n\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00ae\n\17\3\17\3\17"+
		"\3\17\3\17\3\17\5\17\u00b5\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00c7\n\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00f2\n\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\7\20\u00fa\n\20\f\20\16\20\u00fd\13\20\3\21"+
		"\3\21\3\21\7\21\u0102\n\21\f\21\16\21\u0105\13\21\3\22\3\22\3\22\3\22"+
		"\3\22\7\22\u010c\n\22\f\22\16\22\u010f\13\22\3\22\3\22\6\22\u0113\n\22"+
		"\r\22\16\22\u0114\3\22\3\22\3\22\3\22\6\22\u011b\n\22\r\22\16\22\u011c"+
		"\3\22\3\22\3\22\3\22\3\22\6\22\u0124\n\22\r\22\16\22\u0125\3\22\3\22\7"+
		"\22\u012a\n\22\f\22\16\22\u012d\13\22\3\22\3\22\3\22\3\22\3\22\5\22\u0134"+
		"\n\22\3\23\3\23\3\23\2\4\20\36\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$\2\13\3\2(*\3\2\f\r\3\2\17\20\3\2\21\22\3\2\23\25\3\2\26\27\3\2"+
		"\30\33\3\2\34\35\4\2#%++\2\u0163\2,\3\2\2\2\4\61\3\2\2\2\6;\3\2\2\2\b"+
		"I\3\2\2\2\nM\3\2\2\2\fU\3\2\2\2\16Z\3\2\2\2\20b\3\2\2\2\22m\3\2\2\2\24"+
		"q\3\2\2\2\26s\3\2\2\2\30{\3\2\2\2\32~\3\2\2\2\34\u00b4\3\2\2\2\36\u00c6"+
		"\3\2\2\2 \u00fe\3\2\2\2\"\u0133\3\2\2\2$\u0135\3\2\2\2&+\5\4\3\2\'+\5"+
		"\6\4\2(+\5\b\5\2)+\7\3\2\2*&\3\2\2\2*\'\3\2\2\2*(\3\2\2\2*)\3\2\2\2+."+
		"\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\3\3\2\2\2.,\3\2\2\2/\62\7,\2\2\60\62\5\20"+
		"\t\2\61/\3\2\2\2\61\60\3\2\2\2\62\63\3\2\2\2\63\64\7\'\2\2\64\66\7\4\2"+
		"\2\65\67\5\26\f\2\66\65\3\2\2\2\66\67\3\2\2\2\678\3\2\2\289\7\5\2\29:"+
		"\5\32\16\2:\5\3\2\2\2;<\7\67\2\2<=\7\'\2\2=C\7\6\2\2>B\5\b\5\2?B\5\4\3"+
		"\2@B\5\16\b\2A>\3\2\2\2A?\3\2\2\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2"+
		"\2\2DF\3\2\2\2EC\3\2\2\2FG\7\7\2\2GH\7\3\2\2H\7\3\2\2\2IJ\5\20\t\2JK\5"+
		"\n\6\2KL\7\3\2\2L\t\3\2\2\2MR\5\f\7\2NO\7\b\2\2OQ\5\f\7\2PN\3\2\2\2QT"+
		"\3\2\2\2RP\3\2\2\2RS\3\2\2\2S\13\3\2\2\2TR\3\2\2\2UX\7\'\2\2VW\7\t\2\2"+
		"WY\5\36\20\2XV\3\2\2\2XY\3\2\2\2Y\r\3\2\2\2Z[\7\'\2\2[]\7\4\2\2\\^\5\26"+
		"\f\2]\\\3\2\2\2]^\3\2\2\2^_\3\2\2\2_`\7\5\2\2`a\5\32\16\2a\17\3\2\2\2"+
		"bc\b\t\1\2cd\5\24\13\2dj\3\2\2\2ef\f\4\2\2fg\7\n\2\2gi\7\13\2\2he\3\2"+
		"\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\21\3\2\2\2lj\3\2\2\2mn\t\2\2\2n\23"+
		"\3\2\2\2or\5\22\n\2pr\7\'\2\2qo\3\2\2\2qp\3\2\2\2r\25\3\2\2\2sx\5\30\r"+
		"\2tu\7\b\2\2uw\5\30\r\2vt\3\2\2\2wz\3\2\2\2xv\3\2\2\2xy\3\2\2\2y\27\3"+
		"\2\2\2zx\3\2\2\2{|\5\20\t\2|}\7\'\2\2}\31\3\2\2\2~\u0082\7\6\2\2\177\u0081"+
		"\5\34\17\2\u0080\177\3\2\2\2\u0081\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0082\3\2\2\2\u0085\u0086\7\7"+
		"\2\2\u0086\33\3\2\2\2\u0087\u00b5\5\32\16\2\u0088\u00b5\5\b\5\2\u0089"+
		"\u008a\7/\2\2\u008a\u008b\7\4\2\2\u008b\u008c\5\36\20\2\u008c\u008d\7"+
		"\5\2\2\u008d\u0090\5\34\17\2\u008e\u008f\7\60\2\2\u008f\u0091\5\34\17"+
		"\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u00b5\3\2\2\2\u0092\u0093"+
		"\7\61\2\2\u0093\u0095\7\4\2\2\u0094\u0096\5\36\20\2\u0095\u0094\3\2\2"+
		"\2\u0095\u0096\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0099\7\3\2\2\u0098\u009a"+
		"\5\36\20\2\u0099\u0098\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009b\3\2\2\2"+
		"\u009b\u009d\7\3\2\2\u009c\u009e\5\36\20\2\u009d\u009c\3\2\2\2\u009d\u009e"+
		"\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\7\5\2\2\u00a0\u00b5\5\34\17\2"+
		"\u00a1\u00a2\7\62\2\2\u00a2\u00a3\7\4\2\2\u00a3\u00a4\5\36\20\2\u00a4"+
		"\u00a5\7\5\2\2\u00a5\u00a6\5\34\17\2\u00a6\u00b5\3\2\2\2\u00a7\u00a8\7"+
		"\63\2\2\u00a8\u00b5\7\3\2\2\u00a9\u00aa\7\64\2\2\u00aa\u00b5\7\3\2\2\u00ab"+
		"\u00ad\7\65\2\2\u00ac\u00ae\5\36\20\2\u00ad\u00ac\3\2\2\2\u00ad\u00ae"+
		"\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b5\7\3\2\2\u00b0\u00b1\5\36\20\2"+
		"\u00b1\u00b2\7\3\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b5\7\3\2\2\u00b4\u0087"+
		"\3\2\2\2\u00b4\u0088\3\2\2\2\u00b4\u0089\3\2\2\2\u00b4\u0092\3\2\2\2\u00b4"+
		"\u00a1\3\2\2\2\u00b4\u00a7\3\2\2\2\u00b4\u00a9\3\2\2\2\u00b4\u00ab\3\2"+
		"\2\2\u00b4\u00b0\3\2\2\2\u00b4\u00b3\3\2\2\2\u00b5\35\3\2\2\2\u00b6\u00b7"+
		"\b\20\1\2\u00b7\u00b8\7\66\2\2\u00b8\u00c7\5\"\22\2\u00b9\u00ba\t\3\2"+
		"\2\u00ba\u00c7\5\36\20\24\u00bb\u00bc\t\4\2\2\u00bc\u00c7\5\36\20\23\u00bd"+
		"\u00be\t\5\2\2\u00be\u00c7\5\36\20\22\u00bf\u00c0\7\4\2\2\u00c0\u00c1"+
		"\5\36\20\2\u00c1\u00c2\7\5\2\2\u00c2\u00c7\3\2\2\2\u00c3\u00c7\78\2\2"+
		"\u00c4\u00c7\5$\23\2\u00c5\u00c7\7\'\2\2\u00c6\u00b6\3\2\2\2\u00c6\u00b9"+
		"\3\2\2\2\u00c6\u00bb\3\2\2\2\u00c6\u00bd\3\2\2\2\u00c6\u00bf\3\2\2\2\u00c6"+
		"\u00c3\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c5\3\2\2\2\u00c7\u00fb\3\2"+
		"\2\2\u00c8\u00c9\f\21\2\2\u00c9\u00ca\t\6\2\2\u00ca\u00fa\5\36\20\22\u00cb"+
		"\u00cc\f\20\2\2\u00cc\u00cd\t\4\2\2\u00cd\u00fa\5\36\20\21\u00ce\u00cf"+
		"\f\17\2\2\u00cf\u00d0\t\7\2\2\u00d0\u00fa\5\36\20\20\u00d1\u00d2\f\16"+
		"\2\2\u00d2\u00d3\t\b\2\2\u00d3\u00fa\5\36\20\17\u00d4\u00d5\f\r\2\2\u00d5"+
		"\u00d6\t\t\2\2\u00d6\u00fa\5\36\20\16\u00d7\u00d8\f\f\2\2\u00d8\u00d9"+
		"\7\36\2\2\u00d9\u00fa\5\36\20\r\u00da\u00db\f\13\2\2\u00db\u00dc\7\37"+
		"\2\2\u00dc\u00fa\5\36\20\f\u00dd\u00de\f\n\2\2\u00de\u00df\7 \2\2\u00df"+
		"\u00fa\5\36\20\13\u00e0\u00e1\f\t\2\2\u00e1\u00e2\7!\2\2\u00e2\u00fa\5"+
		"\36\20\n\u00e3\u00e4\f\b\2\2\u00e4\u00e5\7\"\2\2\u00e5\u00fa\5\36\20\t"+
		"\u00e6\u00e7\f\7\2\2\u00e7\u00e8\7\t\2\2\u00e8\u00fa\5\36\20\7\u00e9\u00ea"+
		"\f\31\2\2\u00ea\u00fa\t\3\2\2\u00eb\u00ec\f\27\2\2\u00ec\u00ed\7\16\2"+
		"\2\u00ed\u00fa\7\'\2\2\u00ee\u00ef\f\26\2\2\u00ef\u00f1\7\4\2\2\u00f0"+
		"\u00f2\5 \21\2\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\3\2"+
		"\2\2\u00f3\u00fa\7\5\2\2\u00f4\u00f5\f\25\2\2\u00f5\u00f6\7\n\2\2\u00f6"+
		"\u00f7\5\36\20\2\u00f7\u00f8\7\13\2\2\u00f8\u00fa\3\2\2\2\u00f9\u00c8"+
		"\3\2\2\2\u00f9\u00cb\3\2\2\2\u00f9\u00ce\3\2\2\2\u00f9\u00d1\3\2\2\2\u00f9"+
		"\u00d4\3\2\2\2\u00f9\u00d7\3\2\2\2\u00f9\u00da\3\2\2\2\u00f9\u00dd\3\2"+
		"\2\2\u00f9\u00e0\3\2\2\2\u00f9\u00e3\3\2\2\2\u00f9\u00e6\3\2\2\2\u00f9"+
		"\u00e9\3\2\2\2\u00f9\u00eb\3\2\2\2\u00f9\u00ee\3\2\2\2\u00f9\u00f4\3\2"+
		"\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc"+
		"\37\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fe\u0103\5\36\20\2\u00ff\u0100\7\b"+
		"\2\2\u0100\u0102\5\36\20\2\u0101\u00ff\3\2\2\2\u0102\u0105\3\2\2\2\u0103"+
		"\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104!\3\2\2\2\u0105\u0103\3\2\2\2"+
		"\u0106\u010d\5\24\13\2\u0107\u0108\7\n\2\2\u0108\u0109\5\36\20\2\u0109"+
		"\u010a\7\13\2\2\u010a\u010c\3\2\2\2\u010b\u0107\3\2\2\2\u010c\u010f\3"+
		"\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u0112\3\2\2\2\u010f"+
		"\u010d\3\2\2\2\u0110\u0111\7\n\2\2\u0111\u0113\7\13\2\2\u0112\u0110\3"+
		"\2\2\2\u0113\u0114\3\2\2\2\u0114\u0112\3\2\2\2\u0114\u0115\3\2\2\2\u0115"+
		"\u011a\3\2\2\2\u0116\u0117\7\n\2\2\u0117\u0118\5\36\20\2\u0118\u0119\7"+
		"\13\2\2\u0119\u011b\3\2\2\2\u011a\u0116\3\2\2\2\u011b\u011c\3\2\2\2\u011c"+
		"\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\u0134\3\2\2\2\u011e\u0123\5\24"+
		"\13\2\u011f\u0120\7\n\2\2\u0120\u0121\5\36\20\2\u0121\u0122\7\13\2\2\u0122"+
		"\u0124\3\2\2\2\u0123\u011f\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0123\3\2"+
		"\2\2\u0125\u0126\3\2\2\2\u0126\u012b\3\2\2\2\u0127\u0128\7\n\2\2\u0128"+
		"\u012a\7\13\2\2\u0129\u0127\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u0129\3"+
		"\2\2\2\u012b\u012c\3\2\2\2\u012c\u0134\3\2\2\2\u012d\u012b\3\2\2\2\u012e"+
		"\u012f\5\24\13\2\u012f\u0130\7\4\2\2\u0130\u0131\7\5\2\2\u0131\u0134\3"+
		"\2\2\2\u0132\u0134\5\24\13\2\u0133\u0106\3\2\2\2\u0133\u011e\3\2\2\2\u0133"+
		"\u012e\3\2\2\2\u0133\u0132\3\2\2\2\u0134#\3\2\2\2\u0135\u0136\t\n\2\2"+
		"\u0136%\3\2\2\2 *,\61\66ACRX]jqx\u0082\u0090\u0095\u0099\u009d\u00ad\u00b4"+
		"\u00c6\u00f1\u00f9\u00fb\u0103\u010d\u0114\u011c\u0125\u012b\u0133";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}