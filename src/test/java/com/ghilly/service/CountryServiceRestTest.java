package com.ghilly.service;

import com.ghilly.repository.CountryRepositoryRest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CountryServiceRestTest {
    private static final CountryRepositoryRest repository = Mockito.mock(CountryRepositoryRest.class);
    private static final CountryServiceRest service = new CountryServiceRest(repository);
    private static final String name  = "USSR";
    private static final int id  = 1;


    @Test
    void addAndReceiveCountry() {
        Mockito.when(repository.takeCountry(id)).thenReturn(name);

        service.add(name);

        assertAll(
                () -> assertEquals(name, repository.takeCountry(id)),
                () -> Mockito.verify(repository).insert(name)
        );
    }

    @Test
    void receiveList() {
        service.receiveList();

        assertAll(
                () -> Mockito.verify(repository).takeList(),
                () -> Mockito.verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void receiveCountrySuccess() {
        Mockito.when(repository.containsCountry(id)).thenReturn(true);
        Mockito.when(repository.takeCountry(id)).thenReturn(name);

        String expected = service.receiveCountry(id);

        assertAll(
                () -> assertEquals(expected, name),
                () -> Mockito.verify(repository).containsCountry(id),
                () -> Mockito.verify(repository).takeCountry(id),
                () -> Mockito.verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void receiveCountryFail() {
        Mockito.when(repository.containsCountry(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.receiveCountry(id));

        assertAll(
                () -> assertEquals("The country is not found.", exception.getMessage()),
                () -> Mockito.verify(repository).containsCountry(id),
                () -> Mockito.verifyNoMoreInteractions(repository)
        );
    }

    @Test
    void upgradeSuccess() {
    }

    @Test
    void upgradeFail() {
    }

    @Test
    void removeSuccess() {
    }

    @Test
    void removeFail() {
    }
}