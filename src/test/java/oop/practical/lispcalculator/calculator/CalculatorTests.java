package oop.practical.lispcalculator.calculator;

import oop.practical.lispcalculator.lisp.Lisp;
import oop.practical.lispcalculator.lisp.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class CalculatorTests {

    @ParameterizedTest
    @MethodSource
    void testNumber(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testNumber() {
        return Stream.of(
            Arguments.of("Integer", "1", new BigDecimal("1")),
            Arguments.of("Decimal", "1.0", new BigDecimal("1.0")),
            Arguments.of("Precise Decimal", "1.000005", new BigDecimal("1.000005")),
            Arguments.of("Negative Integer", "-5", new BigDecimal("-5")),
            Arguments.of("Negative Decimal", "-2.5", new BigDecimal("-2.5")),
            Arguments.of("Zero", "0", new BigDecimal("0")),
            Arguments.of("Negative Zero", "-0", new BigDecimal("0"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testIdentifier(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testIdentifier() {
        return Stream.of(
            Arguments.of("E", "e", BigDecimal.valueOf(Math.E)),
            Arguments.of("PI", "pi", BigDecimal.valueOf(Math.PI)),
            // An expected value of null means a CalculateException is thrown
            Arguments.of("Undefined", "undefined", null),
            Arguments.of("Identifier Combo", "pie", null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testAdd(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testAdd() {
        return Stream.of(
            Arguments.of("Empty", "(add)", new BigDecimal("0")),
            Arguments.of("Single", "(add 1)", new BigDecimal("1")),
            Arguments.of("Multiple", "(add 1 2 3)", new BigDecimal("6")),
            Arguments.of("Symbol", "(+)", new BigDecimal("0")),
            Arguments.of("Negative", "(add -6 -2)", new BigDecimal("-8")),
            Arguments.of("Mixed Signs", "(add 5 -8)", new BigDecimal("-3")),
            Arguments.of("Decimals", "(add 1.25 3.5)", new BigDecimal("4.75")),
            Arguments.of("Negative Decimals", "(add -2.2 -0.5)", new BigDecimal("-2.7")),
            Arguments.of("Zeros", "(add 0 0)", new BigDecimal("0")),
            Arguments.of("Nested Add", "(add (add 1 2) 3)", new BigDecimal("6")),
            Arguments.of("Nested Operations", "(add (sub 2 1) 3)", new BigDecimal("4"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testSub(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testSub() {
        return Stream.of(
            Arguments.of("Empty", "(sub)", null),
            Arguments.of("Single", "(sub 1)", new BigDecimal("-1")),
            Arguments.of("Multiple", "(sub 1 2 3)", new BigDecimal("-4")),
            Arguments.of("Symbol", "(- 1)", new BigDecimal("-1")),
            Arguments.of("Negative", "(sub -6 -2)", new BigDecimal("-4")),
            Arguments.of("Mixed Signs", "(sub 5 -8)", new BigDecimal("13")),
            Arguments.of("Decimals", "(sub 1.25 3.5)", new BigDecimal("-2.25")),
            Arguments.of("Negative Decimals", "(sub -2.2 -0.5)", new BigDecimal("-1.7")),
            Arguments.of("Zeros", "(sub 0 0)", new BigDecimal("0")),
            Arguments.of("Nested Subtract", "(sub (sub 1 2) 3)", new BigDecimal("-4")),
            Arguments.of("Nested Operations", "(sub (add 2 1) 4)", new BigDecimal("-1"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testMul(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testMul() {
        return Stream.of(
            Arguments.of("Empty", "(mul)", new BigDecimal("1")),
            Arguments.of("Single", "(mul 2)", new BigDecimal("2")),
            Arguments.of("Multiple", "(mul 2 3 4)", new BigDecimal("24")),
            Arguments.of("Symbol", "(*)", new BigDecimal("1")),
            Arguments.of("Large Numbers", "(mul 12000 528000)", new BigDecimal("6336000000")),
            Arguments.of("Negative", "(mul -6 -2)", new BigDecimal("12")),
            Arguments.of("Mixed Signs", "(mul 5 -8)", new BigDecimal("-40")),
            Arguments.of("Decimals", "(mul 1.25 3.5)", new BigDecimal("4.375")),
            Arguments.of("Negative Decimals", "(mul -2.2 -0.5)", new BigDecimal("1.10")),
            Arguments.of("Integers and Decimals", "(mul 10 2.5)", new BigDecimal("25.0")),
            Arguments.of("Zeroes", "(mul 0 0)", new BigDecimal("0")),
            Arguments.of("Nested Multiply", "(mul (mul 1 2) 3)", new BigDecimal("6")),
            Arguments.of("Nested Operations", "(mul (add 2 1) 3)", new BigDecimal("9"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testDiv(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testDiv() {
        return Stream.of(
            Arguments.of("Empty", "(div)", null),
            Arguments.of("Single", "(div 1)", new BigDecimal("1")),
            Arguments.of("Single with Scale", "(div 2.0)", new BigDecimal("0.5")),
            Arguments.of("Rounding", "(div 3)", new BigDecimal("0")),
            Arguments.of("Rounding to Nearest Even", "(div 7 2)", new BigDecimal("4")),
            Arguments.of("Multiple", "(div 1.0 2.0 3.0)", new BigDecimal("0.2")),
            Arguments.of("Symbol", "(/ 2.0)", new BigDecimal("0.5")),
            Arguments.of("Variable", "(div 10 pi)", new BigDecimal("3")),
            Arguments.of("Single Negative", "(div -2.0)", new BigDecimal("-0.5")),
            Arguments.of("Negative", "(div -6 -2)", new BigDecimal("3")),
            Arguments.of("Mixed Signs", "(div 5.00 -8.0)", new BigDecimal("-0.62")),
            Arguments.of("Rounding with Decimal Result", "(div 1.25 3.5)", new BigDecimal("0.36")),
            Arguments.of("Negative Decimals", "(div -2.2 -0.5)", new BigDecimal("4.4")),
            Arguments.of("Mixed Integers and Decimals", "(div 10 2.5)", new BigDecimal("4")),
            Arguments.of("Dividend Zero", "(div 0 3)", new BigDecimal("0")),
            Arguments.of("Divisor Zero", "(div 3 0)", null),
            Arguments.of("Nested Division", "(div (div 8 2) 2)", new BigDecimal("2")),
            Arguments.of("Nested Operations", "(div (sub 8 2) 2)", new BigDecimal("3"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testPow(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testPow() {
        return Stream.of(
            Arguments.of("Empty", "(pow)", null),
            Arguments.of("Single", "(pow 2)", null),
            Arguments.of("2 Arguments", "(pow 2 3)", new BigDecimal("8")),
            Arguments.of("3 Arguments", "(pow 1 2 3)", null),
            Arguments.of("Negative Base", "(pow -2 3)", new BigDecimal("-8")),
            Arguments.of("Negative Exponent", "(pow 2 -3)", null),
            Arguments.of("Decimal Base", "(pow 1.5 3)", new BigDecimal("3.375")),
            Arguments.of("Decimal Exponent", "(pow 2 0.5)", null),
            Arguments.of("Zero Base", "(pow 0 3)", new BigDecimal("0")),
            Arguments.of("Zero Exponent", "(pow 3 0)", new BigDecimal("1")),
            Arguments.of("Comically Large Base", "(pow 12345678900 2)", new BigDecimal("152415787501905210000")),
            Arguments.of("Comically Large Exponent", "(pow 2 12345678900)", null),
            Arguments.of("Comically Large Negative Base", "(pow -12345678900 2)", new BigDecimal("152415787501905210000")),
            Arguments.of("Comically Large Negative Exponent", "(pow -2 12345678900)", null),
            Arguments.of("Nested Pow Base", "(pow (pow 2 3) 2)", new BigDecimal("64"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testSqrt(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testSqrt() {
        return Stream.of(
            Arguments.of("Empty", "(sqrt)", null),
            Arguments.of("Single", "(sqrt 4)", new BigDecimal("2")),
            Arguments.of("Multiple", "(sqrt 2 3)", null),
            Arguments.of("Negative", "(sqrt -16)", null),
            Arguments.of("Decimal", "(sqrt 4.84)", new BigDecimal("2.2")),
            Arguments.of("Redundant Decimals", "(sqrt 4.00)", new BigDecimal("2.0")),
            Arguments.of("Decimal with Rounding", "(sqrt 10)", new BigDecimal("3.2")),
            Arguments.of("Zero", "(sqrt 0)", new BigDecimal("0")),
            Arguments.of("Variable", "(sqrt e)", new BigDecimal("1.648721270700128")),
            Arguments.of("Large Number", "(sqrt 123456789)", new BigDecimal("11111.1111")),
            Arguments.of("Nested Sqrt", "(sqrt (sqrt 81))", new BigDecimal("3"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testRem(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testRem() {
        return Stream.of(
            Arguments.of("Empty", "(rem)", null),
            Arguments.of("Single", "(rem 2)", null),
            Arguments.of("Two Arguments", "(rem 7 2)", new BigDecimal("1")),
            Arguments.of("Three Arguments", "(rem 1.0 2.0 3.0)", null),
            Arguments.of("Variable", "(rem pi 3)", new BigDecimal("0.141592653589793")),
            Arguments.of("Negative Dividend", "(rem -7 2)", new BigDecimal("-1")),
            Arguments.of("Precise Result", "(rem 7.500 2)", new BigDecimal("1.500")),
            Arguments.of("Negative Decimals", "(rem -2.8 -0.5)", new BigDecimal("-0.3")),
            Arguments.of("Integers and Decimals", "(rem 10 3.1)", new BigDecimal("0.7")),
            Arguments.of("Dividend Zero", "(rem 0 3)", new BigDecimal("0")),
            Arguments.of("Divisor Zero", "(rem 3 0)", null),
            Arguments.of("Nested Remainders", "(rem (rem 8 5) 2)", new BigDecimal("1")),
            Arguments.of("Nested Operations", "(rem (sub 8 2) 4)", new BigDecimal("2"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testMod(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testMod() {
        return Stream.of(
            Arguments.of("Empty", "(mod)", null),
            Arguments.of("Single", "(mod 2)", null),
            Arguments.of("Two Arguments", "(mod 19 12)", new BigDecimal("7")),
            Arguments.of("Three Arguments", "(mod 1.0 2.0 3.0)", null),
            Arguments.of("Variable", "(mod pi 3)", new BigDecimal("0.141592653589793")),
            Arguments.of("Negative Dividend", "(mod -19 12)", new BigDecimal("5")),
            Arguments.of("Precise Result", "(mod 7.500 2)", new BigDecimal("1.500")),
            Arguments.of("Decimals", "(mod 2.8 0.5)", new BigDecimal("0.3")),
            Arguments.of("Integers and Decimals", "(mod 10 3.1)", new BigDecimal("0.7")),
            Arguments.of("Dividend Zero", "(mod 0 3)", new BigDecimal("0")),
            Arguments.of("Divisor Zero", "(mod 3 0)", null),
            Arguments.of("Nested Modulos", "(mod (mod 8 5) 2)", new BigDecimal("1")),
            Arguments.of("Nested Operations", "(mod (sub 8 2) 4)", new BigDecimal("2"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testSin(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testSin() {
        return Stream.of(
            Arguments.of("Empty", "(sin)", null),
            Arguments.of("Single Integer", "(sin 1)", new BigDecimal("0.8414709848078965")),
            Arguments.of("Two Arguments", "(sin 19 12)", null),
            Arguments.of("Three Arguments", "(sin 1.0 2.0 3.0)", null),
            Arguments.of("Precise Variable", "(sin pi)", new BigDecimal("1.2246467991473532E-16")),
            Arguments.of("Negative Integer", "(sin -1)", new BigDecimal("-0.8414709848078965")),
            Arguments.of("Excessively Precise Decimal", "(cos 0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001)", null),
            Arguments.of("Zero", "(sin 0)", new BigDecimal("0.0")),
            Arguments.of("Nested Sin", "(sin (sin 1))", new BigDecimal("0.7456241416655579")),
            Arguments.of("Nested Operations", "(sin (- (/ 22 7)))", new BigDecimal("-0.1411200080598672"))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCos(String test, String input, BigDecimal expected) {
        test(input, expected);
    }

    public static Stream<Arguments> testCos() {
        return Stream.of(
            Arguments.of("Empty", "(cos)", null),
            Arguments.of("Single Integer", "(cos 1)", new BigDecimal("0.5403023058681398")),
            Arguments.of("Two Arguments", "(cos 19 12)", null),
            Arguments.of("Three Arguments", "(cos 1.0 2.0 3.0)", null),
            Arguments.of("Precise Variable", "(cos pi)", new BigDecimal("-1.0")),
            Arguments.of("Negative Integer", "(cos -1)", new BigDecimal("0.5403023058681398")),
            Arguments.of("Excessively Precise Decimal", "(cos 0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001)", null),
            Arguments.of("Zero", "(cos 0)", new BigDecimal("1.0")),
            Arguments.of("Nested Cos", "(cos (cos 1))", new BigDecimal("0.8575532158463934")),
            Arguments.of("Nested Operations", "(cos (- (/ 22 7)))", new BigDecimal("-0.9899924966004454"))
        );
    }


    private static void test(String input, BigDecimal expected) {
        if (expected != null) {
            var result = Assertions.assertDoesNotThrow(() -> new Calculator().visit(Lisp.parse(input)));
            Assertions.assertEquals(expected, result);
        } else {
            Assertions.assertThrows(CalculateException.class, () -> new Calculator().visit(Lisp.parse(input)));
        }
    }

}
