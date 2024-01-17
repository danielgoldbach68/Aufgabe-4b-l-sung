/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package edu.kit.kastel;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * This class simulates an Advent calendar containing {@link Candy candies}.
 * 
 * @author Programmieren-Team
 */
public final class AdventCalendar {
    
    private static final int DOORS_PER_LINE = 4;
    private static final String DOOR_REPRESENTATION_FORMAT = "[%s]";
    private static final String CANDY_REPRESENTATION_FORMAT = "%dx%s";
    private static final String EMPTY_DOOR_CONTENT = "   ";

    private int currentDay;
    private final List<Candy> backup;
    private List<Candy> candies;
    private final int maxDays;

    /**
     * {@code false} represents a closed door, {@code true} an opened door.
     * All doors are closed by default.
     */
    private boolean[] doors;

    /**
     * Instantiates a new {@link AdventCalendar}.
     * 
     * @param candies the {@link Candy candies} that are contained in this calendar
     */
    public AdventCalendar(List<Candy> candies) {
        this.backup = new ArrayList<>(candies);
        reset();
        this.maxDays = candies.size();
        this.doors = new boolean[this.maxDays];
        // the initial day is the day before december 1st
        this.currentDay = 0;
    }

    /**
     * Returns the current day.
     * 
     * @return the current day
     */
    public int getDay() {
        return this.currentDay;
    }

    /**
     * Attempts to increase the current day by one day.
     * The day can only be increased if the current day is lower than the 
     * count of objects initialized for this calendar.
     * 
     * @return {@code true} if the current day was increased, {@code false}
     *         otherwise
     */
    public boolean nextDay() {
        return this.nextDays(1);
    }

    /**
     * Attempts to increase the current day by {@code days} days.
     * The day can only be increased if the resulting day will be lower than or
     * equal to the count of objects initialized for this calendar.
     * 
     * @param days the number of days
     * @return {@code true} if the current day was increased, {@code false}
     *         otherwise
     */
    public boolean nextDays(int days) {
        if (days <= 0 || this.currentDay + days > maxDays) {
            return false;
        }

        this.currentDay += days;
        return true;
    }

    /**
     * Determines if the door of a given day is open.
     * 
     * @param number the number/day of the corresponding door
     * @return {@code true} if the door is open, {@code false} otherwise
     */
    public boolean isDoorOpen(int number) {
        return number >= 1 && number <= maxDays && this.doors[number - 1];
    }

    /**
     * Attempts to open the door with the given number.
     * 
     * The door can only be opened if the given number is less then or equal to the
     * current day and if the door has not yet been opened before.
     * 
     * @param number the number of the corresponding door
     * @return the sweet behind the door or {@code null} if the door cannot be or
     *         has already been opened
     */
    public Candy openDoor(int number) {
        if (number < 1 || number > this.currentDay || this.isDoorOpen(number)) {
            return null;
        }

        this.doors[number - 1] = true;
        return this.candies.get(number - 1);
    }

    /**
     * Attempts to open the doors with the given numbers.
     * 
     * The door can only be opened if the given number is less then or equal to the
     * current day and if the door has not yet been opened before.
     * 
     * @param numbers the numbers of the corresponding doors
     * @return the {@link Candy candies} behind the doors that could be opened
     */
    public List<Candy> openDoors(List<Integer> numbers) {
        List<Candy> openedCandies = new ArrayList<>();
        for (Integer number : numbers) {
            if (number < 1 || number > this.currentDay || this.isDoorOpen(number)) {
                continue;
            }

            this.doors[number - 1] = true;
            openedCandies.add(this.candies.get(number - 1));
        }
        return openedCandies;
    }

    /**
     * Returns the number of unopened doors that may be opened at the current day.
     * 
     * @return the number of unopened doors that may be opened at the current day
     */
    public int numberOfUnopenedDoors() {
        int count = 0;
        for (int i = 0; i < this.currentDay; i++) {
            if (!this.doors[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Resets this calender.
     * After the reset all sweets will be restored, all doors will be closed and the
     * current day will be the day before December 1st.
     */
    public void reset() {
        this.currentDay = 0;
        this.doors = new boolean[maxDays];
        this.candies = copyCandyList(this.backup);
    }

    private List<Candy> copyCandyList(List<Candy> toCopy) {
        List<Candy> copy = new ArrayList<>();
        for (Candy candy : toCopy) {
            copy.add(candy.copy());
        }
        return copy;
    }

    @Override
    public String toString() {
        StringJoiner lines = new StringJoiner(System.lineSeparator());
        StringBuilder currentLine = new StringBuilder();
        for (int i = 0; i < maxDays; i++) {
            if (this.doors[i]) {
                currentLine.append(DOOR_REPRESENTATION_FORMAT.formatted(EMPTY_DOOR_CONTENT));
            } else {
                currentLine.append(DOOR_REPRESENTATION_FORMAT.formatted(CANDY_REPRESENTATION_FORMAT
                        .formatted(this.candies.get(i).getQuantity(), this.candies.get(i).getName())));
            }
            
            if (i % DOORS_PER_LINE == DOORS_PER_LINE - 1) {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
            }
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }
        return lines.toString();
    }
}
