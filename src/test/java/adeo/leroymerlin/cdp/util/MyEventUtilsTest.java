/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : MyEventUtilsTest.java
 * Date de création : 16 déc. 2020
 * Heure de création : 17:13:02
 * Package : adeo.leroymerlin.cdp.util
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */	
package adeo.leroymerlin.cdp.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import adeo.leroymerlin.cdp.MyEventTestsUtils;
import adeo.leroymerlin.cdp.model.Member;

/**
 * Unit Test for {@link MyEventUtils}
 * 
 * @author Vincent Otchoun
 */
public class MyEventUtilsTest
{
    /**
     * Test method for {@link adeo.leroymerlin.cdp.util.MyEventUtils#toListSet(java.util.Set)}.
     */
    @Test
    public void testToListSet()
    {
        final Set<Member> members = new HashSet<>();
        members.add(new Member());
        members.add(new Member());
        members.add(new Member());
        members.add(null);
        members.add(null);
        members.add(null);

        final List<Member> list = MyEventUtils.convertSetToList(members);

        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void testToListSet_WithString()
    {
        final Set<String> strings = new HashSet<>();
        strings.add(MyEventTestsUtils.MEMBER_NAME);
        strings.add(MyEventTestsUtils.EVENT_TITLE);
        strings.add(MyEventTestsUtils.MEMBER_NAME);
        strings.add("");
        strings.add(null);
        strings.add(null);
        strings.add(null);

        final List<String> list = MyEventUtils.convertSetToList(strings);

        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    public void testToListSet_ShouldReturnEmptyList()
    {
        final List<String> members = MyEventUtils.convertSetToList(null);

        assertThat(members).isNotNull();
        assertThat(members).isEmpty();
    }

    @Test
    public void testToListSet_ShouldReturnEmptyList2()
    {
        final List<String> members = MyEventUtils.convertSetToList(Collections.emptySet());

        assertThat(members).isNotNull();
        assertThat(members).isEmpty();
    }
}
