package org.simbi.simbiapp.events.Questions;

import org.simbi.simbiapp.api.models.Response.Question;
import org.simbi.simbiapp.events.Event;

import java.util.List;

public class QuestionListEvent extends Event {

    private List<Question> questions;

    public QuestionListEvent(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
