/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : MyEventTestsUtils.java
 * Date de création : 16 déc. 2020
 * Heure de création : 13:31:50
 * Package : adeo.leroymerlin.cdp
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */	
package adeo.leroymerlin.cdp;

import java.util.HashSet;
import java.util.Set;

import adeo.leroymerlin.cdp.model.Band;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.model.Member;

/**
 * Tests Utilities.
 * 
 * @author Vincent Otchoun
 */
public final class MyEventTestsUtils
{
    //
    public static final Long NON_EXIST_ID = 1L;
    public static final Long EXIST_ID = 1002L;
    public static final String MEMBER_NAME = "Queen Anika Walsh";
    public static final String BAND_NAME = "Metallica";
    public static final String EVENT_TITLE = "GrasPop Metal Meeting";

    /**
     * Private constructor for Utilities
     */
    private MyEventTestsUtils()
    {
        //
    }

    /**
     * Create test event instance object.
     * 
     * @return the instance.
     */
    public static Event buildTestEvent()
    {
        // Create Member
        final Member member = MyEventTestsUtils.memberInstanceGet(MyEventTestsUtils.MEMBER_NAME);
        final Set<Member> members = new HashSet<>();
        members.add(member);

        // Create Band
        final Band band = MyEventTestsUtils.bandInstanceGet(MyEventTestsUtils.BAND_NAME, members);
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        return MyEventTestsUtils.eventInstanceGet(MyEventTestsUtils.EVENT_TITLE, bands);
    }

    /**
     * Create test event instance object with null member's.
     * 
     * @return the instance.
     */
    public static Event buildTestEventNullMember()
    {
        // Create Band
        final Band band = MyEventTestsUtils.bandInstanceGet(MyEventTestsUtils.BAND_NAME, null);
        Set<Band> bands = new HashSet<>();
        bands.add(band);

        return MyEventTestsUtils.eventInstanceGet(MyEventTestsUtils.EVENT_TITLE, bands);
    }

    /**
     * Create test event instance object with null band's.
     * 
     * @return the instance.
     */
    public static Event buildTestEventNullBand()
    {
        return MyEventTestsUtils.eventInstanceGet(MyEventTestsUtils.EVENT_TITLE, null);
    }

    /**
     * Create {@link Event} with given name and {@link Band} Set.
     * 
     * @param pEventTitle the title of musical event to be create.
     * @param pBands      the Set of band associated.
     * @return the created musical event object instance.
     */
    private static Event eventInstanceGet(final String pEventTitle, final Set<Band> pBands)
    {
        final Event event = new Event();
        event.setTitle(pEventTitle);
        event.setImgUrl("img/1000.jpeg");
        event.setNbStars(5);
        event.setComment("Add Test Comment");
        event.setBands(pBands);
        return event;
    }

    /**
     * Create {@link Band} with given name and {@link Member} Set.
     * 
     * @param pBandName the name of band to be create.
     * @param pMembers  the Set of member associated.
     * @return the created band object instance.
     */
    private static Band bandInstanceGet(final String pBandName, final Set<Member> pMembers)
    {
        final Band band = new Band();
        band.setName(pBandName);
        band.setMembers(pMembers);
        return band;
    }

    /**
     * Create {@link Member} with given name.
     * 
     * @param pName the name of member to be create.
     * @return the created member object instance.
     */
    private static Member memberInstanceGet(final String pName)
    {
        final Member member = new Member();
        member.setName(pName);
        return member;
    }
}
