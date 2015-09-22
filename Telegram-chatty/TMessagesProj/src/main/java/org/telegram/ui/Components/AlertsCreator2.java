package org.telegram.ui.Components;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import org.telegram.android.LocaleController;
import org.telegram.android.MessagesController;
import org.telegram.android.MessagesStorage;
import org.telegram.android.NotificationsController;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.messenger.R;
import org.telegram.messenger.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;

/**
 * Created by Nam on 2015-09-06.
 */
public class AlertsCreator2 {

    public String domain;

    public static Dialog createTransAlert(Context context, final long dialog_id) {
        if (context == null) {
            return null;
        }
        TranslateCount trans = new TranslateCount();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString("TranslateCountry", R.string.TranslateCountry));

        CharSequence[] items = new CharSequence[]{
                LocaleController.formatString("TransFor", R.string.TransFor, "Afrikaans" + " " + "(" + trans.Afrikaans + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Albanian" + " " + "(" + trans.Albanian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Arabic" + " " + "(" + trans.Arabic + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Armenian" + " " + "(" + trans.Armenian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Azerbaijani" + " " + "(" + trans.Azerbaijani + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Basque" + " " + "(" + trans.Basque + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Belarusian" + " " + "(" + trans.Belarusian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Bengali" + " " + "(" + trans.Bengali + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Bosnian" + " " + "(" + trans.Bosnian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Bulgarian" + " " + "(" + trans.Bulgarian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Catalan" + " " + "(" + trans.Catalan + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Cebuano" + " " + "(" + trans.Cebuano + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Chichewa" + " " + "(" + trans.Chichewa + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Chinese_Simplified" + " " + "(" + trans.Chinese_Simplified + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Chinese_Traditional" + " " + "(" + trans.Chinese_Traditional + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Croatian" + " " + "(" + trans.Croatian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Czech" + " " + "(" + trans.Czech + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Danish" + " " + "(" + trans.Danish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Dutch" + " " + "(" + trans.Dutch + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "English" + " " + "(" + trans.English + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Esperanto" + " " + "(" + trans.Esperanto + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Estonian" + " " + "(" + trans.Estonian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Filipino" + " " + "(" + trans.Filipino + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Finnish" + " " + "(" + trans.Finnish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "French" + " " + "(" + trans.French + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Galician" + " " + "(" + trans.Galician + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Georgian" + " " + "(" + trans.Georgian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "German" + " " + "(" + trans.German + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Greek" + " " + "(" + trans.Greek + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Gujarati" + " " + "(" + trans.Gujarati + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Haitian_Creole" + " " + "(" + trans.Haitian_Creole + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Hausa" + " " + "(" + trans.Hausa + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Hebrew" + " " + "(" + trans.Hebrew + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Hindi" + " " + "(" + trans.Hindi + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Hmong" + " " + "(" + trans.Hmong + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Hungarian" + " " + "(" + trans.Hungarian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Icelandic" + " " + "(" + trans.Icelandic + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Igbo" + " " + "(" + trans.Igbo + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Indonesian" + " " + "(" + trans.Indonesian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Irish" + " " + "(" + trans.Irish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Italian" + " " + "(" + trans.Italian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Japanese" + " " + "(" + trans.Japanese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Javanese" + " " + "(" + trans.Javanese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Kannada" + " " + "(" + trans.Kannada + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Kazakh" + " " + "(" + trans.Kazakh + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Khmer" + " " + "(" + trans.Khmer + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Korean" + " " + "(" + trans.Korean + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Lao" + " " + "(" + trans.Lao + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Latin" + " " + "(" + trans.Latin + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Latvian" + " " + "(" + trans.Latvian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Lithuanian" + " " + "(" + trans.Lithuanian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Macedonian" + " " + "(" + trans.Macedonian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Malagasy" + " " + "(" + trans.Malagasy + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Malay" + " " + "(" + trans.Malay + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Malayalam" + " " + "(" + trans.Malayalam + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Maltese" + " " + "(" + trans.Maltese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Maori" + " " + "(" + trans.Maori + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Marathi" + " " + "(" + trans.Marathi + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Mongolian" + " " + "(" + trans.Mongolian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Myanmar_or_Burmese" + " " + "(" + trans.Myanmar_or_Burmese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Nepali" + " " + "(" + trans.Nepali + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Norwegian" + " " + "(" + trans.Norwegian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Persian" + " " + "(" + trans.Persian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Polish" + " " + "(" + trans.Polish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Portuguese" + " " + "(" + trans.Portuguese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Punjabi" + " " + "(" + trans.Punjabi + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Romanian" + " " + "(" + trans.Romanian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Russian" + " " + "(" + trans.Russian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Serbian" + " " + "(" + trans.Serbian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Sesotho" + " " + "(" + trans.Sesotho + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Sinhala" + " " + "(" + trans.Sinhala + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Slovak" + " " + "(" + trans.Slovak + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Slovenian" + " " + "(" + trans.Slovenian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Somali" + " " + "(" + trans.Somali + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Spanish" + " " + "(" + trans.Spanish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Sudanese" + " " + "(" + trans.Sudanese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Swahili" + " " + "(" + trans.Swahili + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Swedish" + " " + "(" + trans.Swedish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Tajik" + " " + "(" + trans.Tajik + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Tamil" + " " + "(" + trans.Tamil + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Telugu" + " " + "(" + trans.Telugu + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Thai" + " " + "(" + trans.Thai + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Turkish" + " " + "(" + trans.Turkish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Ukrainian" + " " + "(" + trans.Ukrainian + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Urdu" + " " + "(" + trans.Urdu + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Uzbek" + " " + "(" + trans.Uzbek + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Vietnamese" + " " + "(" + trans.Vietnamese + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Welsh" + " " + "(" + trans.Welsh + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Yiddish" + " " + "(" + trans.Yiddish + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Yoruba" + " " + "(" + trans.Yoruba + ")"),
                LocaleController.formatString("TransFor", R.string.TransFor, "Zulu" + " " + "(" + trans.Zulu + ")"),

                //LocaleController.getString("MuteDisable", R.string.MuteDisable)
        };
//        builder.setIsGrid(true);

        builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int untilTime = ConnectionsManager.getInstance().getCurrentTime();
                        if (i == 0) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "af");
                        } else if (i == 1) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "sq");
                        } else if (i == 2) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ar");
                        } else if (i == 3) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "hy");
                        } else if (i == 4) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "az");
                        } else if (i == 5) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "eu");
                        } else if (i == 6) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "be");
                        } else if (i == 7) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "bn");
                        } else if (i == 8) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "bs");
                        } else if (i == 9) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "bg");
                        } else if (i == 10) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ca");
                        } else if (i == 11) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ceb");
                        } else if (i == 12) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ny");
                        } else if (i == 13) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "zh-CN");
                        } else if (i == 14) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "zh-TW");
                        } else if (i == 15) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "hr");
                        } else if (i == 16) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "cs");
                        } else if (i == 17) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "da");
                        } else if (i == 18) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "nl");
                        } else if (i == 19) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "en");
                        } else if (i == 20) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "eo");
                        } else if (i == 21) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "et");
                        } else if (i == 22) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "tl");
                        } else if (i == 23) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "fi");
                        } else if (i == 24) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "fr");
                        } else if (i == 25) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "gl");
                        } else if (i == 26) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ka");
                        } else if (i == 27) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "de");
                        } else if (i == 28) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "el");
                        } else if (i == 29) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "gu");
                        } else if (i == 30) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ht");
                        } else if (i == 31) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ha");
                        } else if (i == 32) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "iw");
                        } else if (i == 33) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "hi");
                        } else if (i == 34) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "hmn");
                        } else if (i == 35) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "hu");
                        } else if (i == 36) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "is");
                        } else if (i == 37) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ig");
                        } else if (i == 38) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "id");
                        } else if (i == 39) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ga");
                        } else if (i == 40) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "it");
                        } else if (i == 41) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ja");
                        } else if (i == 42) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "jw");
                        } else if (i == 43) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "kn");
                        } else if (i == 44) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "kk");
                        } else if (i == 45) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "km");
                        } else if (i == 46) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ko");
                        } else if (i == 47) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "lo");
                        } else if (i == 48) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "la");
                        } else if (i == 49) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "lv");
                        } else if (i == 50) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "lt");
                        } else if (i == 51) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "mk");
                        } else if (i == 52) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "mg");
                        } else if (i == 53) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ms");
                        } else if (i == 54) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ml");
                        } else if (i == 55) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "mt");
                        } else if (i == 56) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "mi");
                        } else if (i == 57) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "mr");
                        } else if (i == 58) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "mn");
                        } else if (i == 59) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "my");
                        } else if (i == 60) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ne");
                        } else if (i == 61) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "no");
                        } else if (i == 62) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "fa");
                        } else if (i == 63) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "pl");
                        } else if (i == 64) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "pt");
                        } else if (i == 65) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ma");
                        } else if (i == 66) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ro");
                        } else if (i == 67) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ru");
                        } else if (i == 68) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "sr");
                        } else if (i == 69) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "st");
                        } else if (i == 70) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "si");
                        } else if (i == 71) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "sk");
                        } else if (i == 72) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "sl");
                        } else if (i == 73) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "so");
                        } else if (i == 74) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "es");
                        } else if (i == 75) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "su");
                        } else if (i == 76) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "sw");
                        } else if (i == 77) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "sv");
                        } else if (i == 78) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "tg");
                        } else if (i == 79) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ta");
                        } else if (i == 80) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "te");
                        } else if (i == 81) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "th");
                        } else if (i == 82) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "tr");
                        } else if (i == 83) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "uk");
                        } else if (i == 84) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "ur");
                        } else if (i == 85) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "uz");
                        } else if (i == 86) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "vi");
                        } else if (i == 87) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "cy");
                        } else if (i == 88) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "yi");
                        } else if (i == 89) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "yo");
                        } else if (i == 90) {
                            ChatActivityEnterView.setLang(LocaleController.getCurrentLanguageName_code(), "zu");
                        }

//                        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", Activity.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        long flags;
//                        if (i == 3) {
//                            editor.putInt("notify2_" + dialog_id, 2);
//                            flags = 1;
//                        } else {
//                            editor.putInt("notify2_" + dialog_id, 3);
//                            editor.putInt("notifyuntil_" + dialog_id, untilTime);
//                            flags = ((long) untilTime << 32) | 1;
//                        }
//                        MessagesStorage.getInstance().setDialogFlags(dialog_id, flags);
//                        editor.commit();
//                        TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialog_id);
//                        if (dialog != null) {
//                            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
//                            dialog.notify_settings.mute_until = untilTime;
//                        }
//                        NotificationsController.updateServerNotificationsSettings(dialog_id);
                    }
                }
        );
        return builder.create();
    }

    public static class TranslateCount {
        public String Afrikaans = "af";
        public String Albanian = "sq";
        public String Arabic = "ar";
        public String Armenian = "hy";
        public String Azerbaijani = "az";
        public String Basque = "eu";
        public String Belarusian = "be";
        public String Bengali = "bn";
        public String Bosnian = "bs";
        public String Bulgarian = "bg";
        public String Catalan = "ca";
        public String Cebuano = "ceb";
        public String Chichewa = "ny";
        public String Chinese_Simplified = "zh-CN";
        public String Chinese_Traditional = "zh-TW";
        public String Croatian = "hr";
        public String Czech = "cs";
        public String Danish = "da";
        public String Dutch = "nl";
        public String English = "en";
        public String Esperanto = "eo";
        public String Estonian = "et";
        public String Filipino = "tl";
        public String Finnish = "fi";
        public String French = "fr";
        public String Galician = "gl";
        public String Georgian = "ka";
        public String German = "de";
        public String Greek = "el";
        public String Gujarati = "gu";
        public String Haitian_Creole = "ht";
        public String Hausa = "ha";
        public String Hebrew = "iw";
        public String Hindi = "hi";
        public String Hmong = "hmn";
        public String Hungarian = "hu";
        public String Icelandic = "is";
        public String Igbo = "ig";
        public String Indonesian = "id";
        public String Irish = "ga";
        public String Italian = "it";
        public String Japanese = "ja";
        public String Javanese = "jw";
        public String Kannada = "kn";
        public String Kazakh = "kk";
        public String Khmer = "km";
        public String Korean = "ko";
        public String Lao = "lo";
        public String Latin = "la";
        public String Latvian = "lv";
        public String Lithuanian = "lt";
        public String Macedonian = "mk";
        public String Malagasy = "mg";
        public String Malay = "ms";
        public String Malayalam = "ml";
        public String Maltese = "mt";
        public String Maori = "mi";
        public String Marathi = "mr";
        public String Mongolian = "mn";
        public String Myanmar_or_Burmese = "my";
        public String Nepali = "ne";
        public String Norwegian = "no";
        public String Persian = "fa";
        public String Polish = "pl";
        public String Portuguese = "pt";
        public String Punjabi = "ma";
        public String Romanian = "ro";
        public String Russian = "ru";
        public String Serbian = "sr";
        public String Sesotho = "st";
        public String Sinhala = "si";
        public String Slovak = "sk";
        public String Slovenian = "sl";
        public String Somali = "so";
        public String Spanish = "es";
        public String Sudanese = "su";
        public String Swahili = "sw";
        public String Swedish = "sv";
        public String Tajik = "tg";
        public String Tamil = "ta";
        public String Telugu = "te";
        public String Thai = "th";
        public String Turkish = "tr";
        public String Ukrainian = "uk";
        public String Urdu = "ur";
        public String Uzbek = "uz";
        public String Vietnamese = "vi";
        public String Welsh = "cy";
        public String Yiddish = "yi";
        public String Yoruba = "yo";
        public String Zulu = "zu";







    }
}
