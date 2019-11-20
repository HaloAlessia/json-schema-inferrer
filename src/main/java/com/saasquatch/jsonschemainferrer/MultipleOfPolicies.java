package com.saasquatch.jsonschemainferrer;

import static com.saasquatch.jsonschemainferrer.JunkDrawer.allNumbersAreIntegers;
import java.math.BigInteger;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Utilities for {@link MultipleOfPolicy}
 *
 * @author sli
 */
public final class MultipleOfPolicies {

  private MultipleOfPolicies() {}

  /**
   * @return a singleton {@link MultipleOfPolicy} that does nothing.
   */
  public static MultipleOfPolicy noOp() {
    return input -> null;
  }

  /**
   * @return a singleton {@link MultipleOfPolicy} that uses the GCD of number samples as
   *         {@code multipleOf}.
   */
  public static MultipleOfPolicy gcd() {
    return input -> {
      // Only proceed if all numbers are integers
      if (!Consts.Types.INTEGER.equals(input.getType())) {
        final boolean allNumbersAreIntegers = allNumbersAreIntegers(input.getSamples());
        if (!allNumbersAreIntegers) {
          return null;
        }
      }
      return input.getSamples().stream()
          .filter(JsonNode::isNumber)
          .map(JsonNode::bigIntegerValue)
          .reduce(BigInteger::gcd)
          .filter(gcd -> !BigInteger.ZERO.equals(gcd))
          .map(JunkDrawer::numberNode)
          .orElse(null);
    };
  }

}