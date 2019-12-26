package com.example.myapplication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONdecodeur {
        private String numTel, txtSMS;
        private boolean isParsed = false;
        /**
         * Methode d'analyse d'une String au format JSON
         * et qui extrait les valeurs des champs "number" et "text"
         *
         * @param jsonString
         */
        JSONdecodeur(String jsonString) {
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
                // retourne un JSONObject qui est la valeur du champ SMS
                JSONObject jsonSMS = (JSONObject) jsonObject.get("SMS");
                if (jsonSMS != null) {
                    numTel = (String) jsonSMS.get("number");  // retourne la valeur du champ number
                    txtSMS = (String) jsonSMS.get("text");    // retourne la valeur du champ text
                    if (numTel != null && txtSMS != null) {
                        isParsed = true;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * Retourne le numero du SMS à envoyer, extrait du code JSON
         *
         * @return String numero du SMS
         */
        String getNumTel() {return numTel;}
        /**
         * Retourne le texte du SMS à envoyer, extrait du code JSON
         *
         * @return String message du SMS
         */
        String getTxtSMS() {return txtSMS;}
        /**
         * Retourne true si le traitement est correct et exploitable
         *
         * @return Boolean
         */
        Boolean isParsed() {return isParsed;}
    }