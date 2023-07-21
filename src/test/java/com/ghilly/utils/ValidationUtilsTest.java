package com.ghilly.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ValidationUtilsTest {

    private static Stream<Arguments> provideNames() {
        return Stream.of(
                Arguments.of("USA", false),
                Arguments.of("germanY", false),
                Arguments.of("Bosnia and Herzegovina", false),
                Arguments.of("Guinea-Bissau", false),
                Arguments.of("Guinea-Bissau-old-old", false),
                Arguments.of("Germany*", true),
                Arguments.of("77Russia", true),
                Arguments.of("U.S.A", true),
                Arguments.of("Bosnia  and Herzegovina", true),
                Arguments.of("   ", true),
                Arguments.of("", true),
                Arguments.of("Guinea-Bissau--old", true));
    }


    @ParameterizedTest
    @MethodSource("provideNames")
    void correctNamesCheckingTest(String name, boolean expected) {
        assertEquals(ValidationUtils.isWrongName(name), expected);
    }
}