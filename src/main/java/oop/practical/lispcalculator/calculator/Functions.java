package oop.practical.lispcalculator.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

final class Functions {

    static BigDecimal add(List<BigDecimal> arguments) {
        var result = BigDecimal.ZERO;
        for (var number : arguments) {
            result = result.add(number);
        }
        return result;
    }

    static BigDecimal sub(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.isEmpty())
            throw new CalculateException("Function sub requires at least one argument.");
        if (arguments.size() == 1)
            return BigDecimal.ZERO.subtract(arguments.getFirst());
        var result = arguments.getFirst();
        for (int i = 1; i < arguments.size(); i++) {
            result = result.subtract(arguments.get(i));
        }
        return result;
    }

    static BigDecimal mul(List<BigDecimal> arguments) {
        var result = BigDecimal.ONE;
        for (var number : arguments) {
            result = result.multiply(number);
        }
        return result;
    }

    static BigDecimal div(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.isEmpty())
            throw new CalculateException("Function div requires at least one argument.");
        else if (arguments.size() == 1) {
            var result = BigDecimal.ONE;
            var number = arguments.getFirst();
            if (number.equals(BigDecimal.ZERO))
                throw new CalculateException("Cannot divide by zero.");
            return result.divide(number, number.scale(), RoundingMode.HALF_EVEN);
        }
        else {
            var result = arguments.getFirst();
            for (int i = 1; i < arguments.size(); i++) {
                var number = arguments.get(i);
                if (number.equals(BigDecimal.ZERO))
                    throw new CalculateException("Cannot divide by zero.");
                result = result.divide(number, RoundingMode.HALF_EVEN);
            }
            return result;
        }
    }

    static BigDecimal pow(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.size() != 2)
            throw new CalculateException("Function pow requires two arguments.");
        else {
            var base = arguments.getFirst();
            var exponent = arguments.getLast();
            if (exponent.scale() != 0 || exponent.intValue() < 0 ||             // exponent is fractional or negative
                exponent.compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0 ||    // exponent is too large to convert to int
                exponent.compareTo(new BigDecimal(Integer.MIN_VALUE)) < 0)      // exponent is too negative to convert to int
                throw new CalculateException("Function pow's second argument must be a non-negative integer.");
            var result = BigDecimal.ONE;
            for (int i = 0; i < exponent.intValue(); i++) {
                result = result.multiply(base);
            }
            return result;
        }
    }

    static BigDecimal sqrt(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.size() != 1)
            throw new CalculateException("Function sqrt requires exactly one argument.");
        else {
            var number = arguments.getFirst();
            if (number.intValue() < 0)
                throw new CalculateException(("Function sqrt's argument must be non-negative"));
            if (number.equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO;
            }
            return number.sqrt(new MathContext(number.precision(), RoundingMode.HALF_EVEN));
        }
    }

    static BigDecimal rem(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.size() != 2)
            throw new CalculateException("Function rem requires two arguments.");
        var dividend = arguments.getFirst();
        var divisor = arguments.getLast();
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new CalculateException("Cannot divide by zero");
        }
        return dividend.remainder(divisor);
    }

    static BigDecimal mod(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.size() != 2)
            throw new CalculateException("Function mod requires two arguments.");
        var dividend = arguments.getFirst();
        var divisor = arguments.getLast();
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new CalculateException("Cannot divide by zero");
        }
        var remainder = dividend.remainder(divisor);
        if (remainder.compareTo(BigDecimal.ZERO) < 0) {
            remainder = remainder.add(divisor);
        }
        return remainder;
    }

    static BigDecimal sin(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.size() != 1)
            throw new CalculateException("Function sin requires exactly one argument");
        var number = arguments.getFirst();
        if (number.compareTo(new BigDecimal(Double.MAX_VALUE)) > 0 ||           // argument too large to convert to double
            number.compareTo(BigDecimal.valueOf(-Double.MAX_VALUE)) < 0 ||      // argument too negative to convert to double
            number.compareTo(BigDecimal.valueOf(number.doubleValue())) != 0)    // argument too precise to convert to double
            throw new CalculateException("Value cannot be represented by a double.");
        return BigDecimal.valueOf(Math.sin(number.doubleValue()));
    }

    static BigDecimal cos(List<BigDecimal> arguments) throws CalculateException {
        if (arguments.size() != 1)
            throw new CalculateException("Function sin requires exactly one argument");
        var number = arguments.getFirst();
        if (number.compareTo(new BigDecimal(Double.MAX_VALUE)) > 0 ||           // argument too large to convert to double
            number.compareTo(BigDecimal.valueOf(-Double.MAX_VALUE)) < 0 ||      // argument too negative to convert to double
            number.compareTo(BigDecimal.valueOf(number.doubleValue())) != 0)    // argument too precise to convert to double
            throw new CalculateException("Value cannot be represented by a double.");
        return BigDecimal.valueOf(Math.cos(number.doubleValue()));
    }
}
