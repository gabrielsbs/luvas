package com.labbbio.luvas;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


public class TalkBackLuvas extends AccessibilityService {


    private static final String MESSAGE_FROM_TALKBACKLUVAS = "TalkBackLuvastoMain";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){

        int t = event.getEventType();
        boolean listview=false;
        String msg;

        StringBuilder sb=new StringBuilder();

        //filtra os eventos desejados
        switch (t) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:


                //caso seja listview, anunciar a lista e o numero de elementos
                if (event.getClassName().equals("android.widget.ListView")){


                    if(event.getEventType()==event.TYPE_VIEW_SELECTED){
                        //&& source.getText() != null && !source.getText().toString().isEmpty()
                        AccessibilityNodeInfo source = event.getSource();
                        if (source == null) {
                            return;
                        }
                        int level = source.getChildCount();


                        if (level > 0 && source.getClassName().equals("android.widget.ListView") ) {
                            if(event.getCurrentItemIndex()==0) {
                                listview=true;
                                int count = source.getChildCount();
                                sb.append("Lista contendo " + count+" elemento");
                                if(count>1)sb.append("s");
                                Log.i("servicodeacessibilidade", sb.toString());

                                //SendAccessiblityMessage(sb.toString());
                                if (source.getChild(0).getText() != null)
                                    Log.i("servicodeacessibilidade", source.getChild(0).getText().toString());
                            }else{
                                if (source.getChild(event.getCurrentItemIndex()).getText() != null)
                                    Log.i("servicodeacessibilidade", source.getChild(event.getCurrentItemIndex()).getText().toString());
                            }

//                            if (source.getClassName().equals("android.widget.TextView") && source.getParent() != null && source.getParent().getClassName().equals("android.widget.FrameLayout") && source.getParent().getParent() == null) {
//                          }
                        }}
                }



//Selecao entre text ou content description
                if (!(event.getText().toString().startsWith("[]"))) {

                    msg=event.getText().toString();
                    msg=msg.substring(1, msg.length()-1);

                    if(listview)msg=sb.append(" " + msg).toString();
                    SendAccessiblityMessage(msg);

                    Log.i("servicodeacessibilidade", "text: "+AccessibilityEvent.eventTypeToString(t) +" - " + msg);



                } else {if(event.getContentDescription()!=null) {
                    msg=event.getContentDescription().toString();
                    Log.i("servicodeacessibilidade", "contdesc: " + AccessibilityEvent.eventTypeToString(t) + " - " + msg);
                    SendAccessiblityMessage(msg);

                }

                }

                break;
        }



    }


    @Override
    public void onInterrupt(){
        Log.i("servicodeacessibilidade", "Interrupcao no servico de acessibilidade");

    }


    //Função que envia a mensagem de acessibilidade para o programa principal para poder ser encaminhado à luva
    private void SendAccessiblityMessage(String msg){
        Log.i("servicodeacessibilidade", "ENVIANDO: " + msg);
        Intent toMain = new Intent();
        toMain.putExtra("acessibilidade", msg);
        toMain.setAction(MESSAGE_FROM_TALKBACKLUVAS);
        sendBroadcast(toMain);
    }




}
