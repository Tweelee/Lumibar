package edu.tsp.asr.lumibar;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Created by mps on 10/01/16.
 */
public class TweetEngine {

    /** retourne le nombre d'occurences du mot but
    *   dans les tweets renvoyés par la requête concernant le compte demandé dans la requête post du main
     * mesure la "ferveur"
     */
    public int searchTweets(Query query, String word) {
        if (query == null) {
            System.out.println("Write a correct query");
        }

        int nb =0;

        // Login to Twitter with account owned by Mare_Petra
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("W1ikYacYZPCywEnY8lotqTjoZ")
                .setOAuthConsumerSecret("CcRlc0aVH8khGDSiMvqRurS1L4AhGwNV02yWIf7RcrwWZU4fo4")
                .setOAuthAccessToken("3237791733-W0ifkl6qpyKJkY3B9UtBXMsUDheSmqSn21CnWcG")
                .setOAuthAccessTokenSecret("7rLepfZnjHmgGL4K6zgDCwusbPMlySqp0qOuOrHBqavPi");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName() + " : " + status.getText());
                if ( status.getText().contains(word)){
                    nb++;
                }
            }

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        return nb;
    }

}
