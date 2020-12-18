/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : MyEventCustomExceptionTest.java
 * Date de création : 18 déc. 2020
 * Heure de création : 08:27:52
 * Package : adeo.leroymerlin.cdp.error
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */	
package adeo.leroymerlin.cdp.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import adeo.leroymerlin.cdp.enumration.ErrorCodeEnum;

/**
 * Unit test Class for {@link MyEventCustomException}
 * 
 * @author Vincent Otchoun
 */
public class MyEventCustomExceptionTest
{

    @Test
    public void test()
    {
        final MyEventCustomException customException = new MyEventCustomException();
        customException.setStatus(HttpStatus.BAD_GATEWAY);
        customException.setMessage(ErrorCodeEnum.CREATE_EVENT_ERROR.name());
        customException.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));
        customException.setErrorCode(ErrorCodeEnum.CREATE_EVENT_ERROR.name());

        assertThat(customException).isNotNull();
        assertThat(customException).isExactlyInstanceOf(MyEventCustomException.class);
        assertThat(customException.getMessage()).isExactlyInstanceOf(String.class);
        assertThat(customException.getStatus()).isExactlyInstanceOf(HttpStatus.class);
        assertThat(customException.getTimestamp()).isExactlyInstanceOf(LocalDateTime.class);
        assertThat(customException.getErrorCode()).isExactlyInstanceOf(String.class);
    }

}
