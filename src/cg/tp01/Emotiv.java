/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.tp01;

import com.emotiv.Iedk.*;
import com.emotiv.Iedk.EmoState.IEE_FacialExpressionAlgo_t;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjd
 */
public class Emotiv {
    
    private final Paint paint;
    
    public Emotiv(final Paint paint) {
        this.paint = paint;
    }
    
    public static void test() {
        Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
		Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();
		IntByReference userID = null;
		short composerPort = 1726;
		int option = 2;
		int state = 0;
                double sense = 0.2;

		userID = new IntByReference(0);

		switch (option) {
		case 1: {
			if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK
					.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				return;
			}
			break;
		}
		case 2: {
			System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

			if (Edk.INSTANCE.IEE_EngineRemoteConnect("127.0.0.1", composerPort,
					"Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out
						.println("Cannot connect to EmoComposer on [127.0.0.1]");
				return;
			}
			System.out.println("Connected to EmoComposer on [127.0.0.1]");
			break;
		}
		default:
			System.out.println("Invalid option...");
			return;
		}

		while (true) {
                    //MainWindow.rightWink = false;
			state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);
                         

			// New event needs to be handled
			if (state == EdkErrorCode.EDK_OK.ToInt()) {

				int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
				Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);

				// Log the EmoState if it has been updated
				if (eventType == Edk.IEE_Event_t.IEE_EmoStateUpdated.ToInt()) {

					Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(eEvent, eState);
					float timestamp = EmoState.INSTANCE
							.IS_GetTimeFromStart(eState);
					/*System.out.println(timestamp + " : New EmoState from user "
							+ userID.getValue());*/

					/*System.out.print("WirelessSignalStatus: ");*/
					EmoState.INSTANCE.IS_GetWirelessSignalStatus(eState);

					if (EmoState.INSTANCE.IS_FacialExpressionIsBlink(eState) == 1){
						//System.out.println("Blink");
                                        }
                                        
                                        
					if (EmoState.INSTANCE.IS_FacialExpressionIsLeftWink(eState) == 1){
						//System.out.println("LeftWink");
                                            MainWindow.winkLeft = true;
                                            try {
                                                Thread.sleep(250);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            MainWindow.winkLeft = false;
                                        }
                                        
                                        
					if (EmoState.INSTANCE
							.IS_FacialExpressionIsRightWink(eState) == 1){
						//System.out.println("RightWink");  
                                            MainWindow.winkRight = true;
                                            try {
                                                Thread.sleep(250);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            MainWindow.winkRight = false;
                                        }
                                        
                                        
                                        if (EmoState.INSTANCE
							.IS_FacialExpressionIsLookingLeft(eState) == 1){
						//System.out.println("LookLeft");  
                                            MainWindow.lookLeft = true;
                                            try {
                                                Thread.sleep(250);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            MainWindow.lookLeft = false;
                                        }
                                        
                                        
                                        if (EmoState.INSTANCE
							.IS_FacialExpressionIsLookingRight(eState) == 1){
						//System.out.println("LookRight");  
                                            MainWindow.lookRight = true;
                                            try {
                                                Thread.sleep(250);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            MainWindow.lookRight = false;
                                        }
                                        
                                        
                                        if (EmoState.INSTANCE
                                                        .IS_FacialExpressionGetClenchExtent(eState) > sense){
                                            //System.out.println("Clench");
                                            MainWindow.clench = true;
                                            try {
                                                Thread.sleep(20);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                                
                                        } else {
                                            MainWindow.clench = false;                                            
                                        }
                                        
                                        
                                        if (EmoState.IEE_FacialExpressionAlgo_t.FE_SMIRK_RIGHT.ToInt() == EmoState.INSTANCE
                                                .IS_FacialExpressionGetLowerFaceAction(eState)){
                                            
                                            if (EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceActionPower(eState) > sense){
                                                MainWindow.smirkRight = true;
                                                //System.out.println("Smirk Right");
                                                 try {
                                                      Thread.sleep(250);
                                                 } catch (InterruptedException ex) {
                                                    Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                                 }
                                            } else {
                                                MainWindow.smirkRight = false;
                                            }
                                        }
                                            
                                            
                                        if (EmoState.IEE_FacialExpressionAlgo_t.FE_SMIRK_LEFT.ToInt() == EmoState.INSTANCE
                                                .IS_FacialExpressionGetLowerFaceAction(eState)){
                                            
                                            if (EmoState.INSTANCE.IS_FacialExpressionGetLowerFaceActionPower(eState) > sense){
                                                MainWindow.smirkLeft = true;
                                                //System.out.println("Smirk Left");
                                                 try {
                                                      Thread.sleep(250);
                                                 } catch (InterruptedException ex) {
                                                    Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                                 }
                                            } else {
                                                MainWindow.smirkLeft = false;
                                            }                                            
                                        }
                                        
                                        
                                        if (EmoState.IEE_FacialExpressionAlgo_t.FE_SURPRISE.ToInt() == EmoState.INSTANCE
                                                .IS_FacialExpressionGetUpperFaceAction(eState)){
                                            
                                            if (EmoState.INSTANCE.IS_FacialExpressionGetUpperFaceActionPower(eState) > sense){
                                                MainWindow.raiseBrow = true;
                                                //System.out.println("Raise Brow");
                                                 try {
                                                      Thread.sleep(250);
                                                 } catch (InterruptedException ex) {
                                                    Logger.getLogger(Emotiv.class.getName()).log(Level.SEVERE, null, ex);
                                                 }
                                            } else {
                                                MainWindow.raiseBrow = false;
                                            }                                            
                                        }             
                                }
			} else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
				System.out.println("Internal error in Emotiv Engine!");
				break;
			}
		}
		Edk.INSTANCE.IEE_EngineDisconnect();
		System.out.println("Disconnected!");
    }
}
