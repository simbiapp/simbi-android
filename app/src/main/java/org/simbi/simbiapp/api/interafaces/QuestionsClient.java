package org.simbi.simbiapp.api.interafaces;

import com.squareup.otto.Bus;

public interface QuestionsClient {

    public void getAllQuestions(String token);

    public Bus getBus();


}
