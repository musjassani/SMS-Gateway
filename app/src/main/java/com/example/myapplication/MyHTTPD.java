package com.example.myapplication;

import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.TextView;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

class MyHTTPD extends NanoHTTPD {
        private Handler handler; // necessaire pour actualiser l'UI
        private TextView numWidget;
        private TextView txtWidget;
        final int MAXCHAR = 200;  // taille maximale du JSON recu

        /**
         * Constructeur qui initialise les variables
         * et cree un handler pour se connecter a l'UI
         */
        public MyHTTPD(int port, TextView numWid, TextView textWid) throws IOException {
            super(port);
            this.numWidget = numWid;
            this.txtWidget = textWid;
            handler = new Handler();
        }

        @Override
        public Response serve(IHTTPSession session) {
            final JSONdecodeur jsonDec;
            byte[] messageDuClient = new byte[MAXCHAR];
            int tailleMessageClient = 0;
            try {  // conversion d'un tableau en String
                tailleMessageClient = session.getInputStream().read(messageDuClient,0,MAXCHAR);
                String jsonMsg = new String(messageDuClient);
                jsonMsg = jsonMsg.substring(0, tailleMessageClient);
                jsonDec = new JSONdecodeur(jsonMsg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {   // affiche le numero et le texte du SMS envoye
                        if (jsonDec.isParsed()) {  // seulement si JSON valide
                            numWidget.setText("Numéro : " + jsonDec.getNumTel()); // met a jour l'UI
                            txtWidget.setText("Message : " + jsonDec.getTxtSMS());
                            SmsManager smsManager = SmsManager.getDefault();  // envoi du SMS
                            smsManager.sendTextMessage(jsonDec.getNumTel(), null, jsonDec.getTxtSMS(),
                                    null, null);
                        }
                        else {
                            numWidget.setText("Format du JSON"); // met a jour l'UI
                            txtWidget.setText("Revoir la syntaxe");
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            // renvoie code de fin au client
            return newFixedLengthResponse(java.net.HttpURLConnection.HTTP_OK + "\n");
        }
    }