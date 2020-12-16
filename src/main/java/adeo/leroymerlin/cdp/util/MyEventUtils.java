/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : MyEventUtils.java
 * Date de création : 16 déc. 2020
 * Heure de création : 17:00:10
 * Package : adeo.leroymerlin.cdp.util
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */	
package adeo.leroymerlin.cdp.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * My Event App Utilities.
 * 
 * @author Vincent Otchoun
 */
public final class MyEventUtils
{
    /**
     * Utilities private constrcutor.
     */
    private MyEventUtils()
    {
    }

    /**
     * Convert Set to List.
     * 
     * @param <T>                  element type.
     * @param pObjectListToBeBound a set to be converted.
     * @return a list of element otherwise empty list.
     */
    public static <T> List<T> convertSetToList(final Set<T> pObjectSetToBeBound)
    {
        return Optional.ofNullable(pObjectSetToBeBound)//
        .orElseGet(Collections::emptySet)//
        .stream()//
        .filter(Objects::nonNull)//
        .collect(Collectors.toList());
    }
}
