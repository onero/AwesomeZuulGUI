package be;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Challenge {

    private final String mChallengeName;
    private final String mChallengeDescription;
    private final List<String> mAnswers;
    private final String mRightAnswer;

    /**
     * Creates the challenge with the assigned name and description
     *
     * @param challengeName
     * @param challengeDescription
     * @param rightAnswer
     */
    public Challenge(String challengeName, String challengeDescription, String rightAnswer) {
        mChallengeName = challengeName;
        mChallengeDescription = challengeDescription;
        mRightAnswer = rightAnswer;
        mAnswers = new ArrayList<>();
        mAnswers.add(rightAnswer);
    }

    /**
     * Gets the name of the challenge
     *
     * @return
     */
    public String getChallengeName() {
        return mChallengeName;
    }

    /**
     * Gets the full challenge
     *
     * @return
     */
    public String getChallenge() {
        String challenge = "This room has the following challenge!\n";
        challenge += mChallengeName + ": ";
        challenge += mChallengeDescription + "\n\n";
        challenge += "Choose your answer wisely:\n";
        //TODO ALH: make this better
        for (int i = 0; i < mAnswers.size(); i++) {
            for (String mAnswer : mAnswers) {
                challenge += "Type " + i + ") " + mAnswer + "\n";
                i++;
            }
        }
        return challenge;
    }

    /**
     * Checks if the challenge is passed
     *
     * @param answer
     * @return
     */
    public boolean isChallengePassed(String answer) {
        boolean isAnswerCorrect;
        if (answer.equalsIgnoreCase("0")) {
            isAnswerCorrect = true;
        } else {
            isAnswerCorrect = false;
        }
        return isAnswerCorrect;
    }

    /**
     * Adds false answers for the question
     *
     * @param answer
     */
    public void addFakeAnswer(String answer) {
        mAnswers.add(answer);
    }
}
