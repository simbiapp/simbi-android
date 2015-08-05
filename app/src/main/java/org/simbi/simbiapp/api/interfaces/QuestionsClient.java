package org.simbi.simbiapp.api.interfaces;

import com.squareup.otto.Bus;

public interface QuestionsClient {

    public void getAllQuestions(String token);

    public Bus getBus();


}
