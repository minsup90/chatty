/*
 * This is the source code of Telegram for Android v. 1.3.2.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013.
 */

package org.telegram.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.telegram.android.AndroidUtilities;
import org.telegram.messenger.ConnectionsManager;
import org.telegram.android.LocaleController;
import org.telegram.android.MessagesStorage;
import org.telegram.messenger.TLRPC;
import org.telegram.messenger.FileLog;
import org.telegram.android.MessagesController;
import org.telegram.android.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.Adapters.BaseFragmentAdapter;
import org.telegram.ui.Cells.GreySectionCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarUpdater;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.FrameLayoutFixed;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GroupCreateFinalActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, AvatarUpdater.AvatarUpdaterDelegate {

    private ListAdapter listAdapter;
    private ListView listView;
    private EditText nameTextView;
    private TLRPC.FileLocation avatar;
    private TLRPC.InputFile uploadedAvatar;
    private ArrayList<Integer> selectedContacts;
    private BackupImageView avatarImage;
    private AvatarDrawable avatarDrawable;
    private boolean createAfterUpload;
    private boolean donePressed;
    private AvatarUpdater avatarUpdater = new AvatarUpdater();
    private ProgressDialog progressDialog = null;
    private String nameToSet = null;
    private boolean isBroadcast = false;

    private final static int done_button = 1;

    public GroupCreateFinalActivity(Bundle args) {
        super(args);
        isBroadcast = args.getBoolean("broadcast", false);
        avatarDrawable = new AvatarDrawable();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.chatDidFailCreate);
        avatarUpdater.parentFragment = this;
        avatarUpdater.delegate = this;
        selectedContacts = getArguments().getIntegerArrayList("result");
        final ArrayList<Integer> usersToLoad = new ArrayList<>();
        for (Integer uid : selectedContacts) {
            if (MessagesController.getInstance().getUser(uid) == null) {
                usersToLoad.add(uid);
            }
        }
        if (!usersToLoad.isEmpty()) {
            final Semaphore semaphore = new Semaphore(0);
            final ArrayList<TLRPC.User> users = new ArrayList<>();
            MessagesStorage.getInstance().getStorageQueue().postRunnable(new Runnable() {
                @Override
                public void run() {
                    users.addAll(MessagesStorage.getInstance().getUsers(usersToLoad));
                    semaphore.release();
                }
            });
            try {
                semaphore.acquire();
            } catch (Exception e) {
                FileLog.e("tmessages", e);
            }
            if (usersToLoad.size() != users.size()) {
                return false;
            }
            if (!users.isEmpty()) {
                for (TLRPC.User user : users) {
                    MessagesController.getInstance().putUser(user, true);
                }
            } else {
                return false;
            }
        }
        return super.onFragmentCreate();
    }

    // Destroy
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.chatDidFailCreate);
        avatarUpdater.clear();
    }

    // Resume
    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            //Android에서 ListView의 데이터가 변경되면, Adapter의 notifyDataSetChanged() 메소드를 호출해서 새로운 데이터를
            // ListView에 나타나도록 한다.
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        if (isBroadcast) {
            actionBar.setTitle(LocaleController.getString("NewBroadcastList", R.string.NewBroadcastList));
        } else {
            actionBar.setTitle(LocaleController.getString("NewGroup", R.string.NewGroup));
        }

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            // 액션 바 메뉴를 클릭했을 때만 해당
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    // <- 버튼을 눌렀을 때
                    finishFragment();
                } else if (id == done_button) {
                    // 체크 버튼을 눌렀을 때
                    if (donePressed) {
                        return;
                    }
                    if (nameTextView.getText().length() == 0) {
                        // 그룹 이름 입력 칸을 입력하지 않앗을 때
                        return;
                    }
                    donePressed = true;

                    if (isBroadcast) {
                        MessagesController.getInstance().createChat(nameTextView.getText().toString(), selectedContacts, isBroadcast);
                    } else {
                        if (avatarUpdater.uploadingAvatar != null) {
                            createAfterUpload = true;
                        } else {
                            // 그룹 대화방 생성 단계
                            progressDialog = new ProgressDialog(getParentActivity());
                            // "불러오는 중..."
                            progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setCancelable(false);

                            // nameTextView.getText().toString(): 그룹방 제목, selectedContacts: 그룹에 속한 사람들, isBroadcast: 단체 메시지 -> 0
                            final long reqId = MessagesController.getInstance().createChat(nameTextView.getText().toString(), selectedContacts, isBroadcast);

                            // cancel: "취소"
                            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ConnectionsManager.getInstance().cancelRpc(reqId, true);
                                    donePressed = false;
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        FileLog.e("tmessages", e);
                                    }
                                }
                            });
                            progressDialog.show();
                        }
                    }
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        // ic_done: 체크 버튼
        menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

        // 대화상대 목록을 수직 방향으로 나열
        fragmentView = new LinearLayout(context);
        LinearLayout linearLayout = (LinearLayout) fragmentView;
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout frameLayout = new FrameLayoutFixed(context);  // FrameLayoutFixed는 FrameLayout을 상속받음
        linearLayout.addView(frameLayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.height = LayoutHelper.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        frameLayout.setLayoutParams(layoutParams);

        // BackupImageView 는 View 클래스를 상속받음
        avatarImage = new BackupImageView(context);
        avatarImage.setRoundRadius(AndroidUtilities.dp(32));
        avatarDrawable.setInfo(5, null, null, isBroadcast);
        avatarImage.setImageDrawable(avatarDrawable);
        frameLayout.addView(avatarImage);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) avatarImage.getLayoutParams();
        layoutParams1.width = AndroidUtilities.dp(64);
        layoutParams1.height = AndroidUtilities.dp(64);
        layoutParams1.topMargin = AndroidUtilities.dp(12);
        layoutParams1.bottomMargin = AndroidUtilities.dp(12);
        layoutParams1.leftMargin = LocaleController.isRTL ? 0 : AndroidUtilities.dp(16);
        layoutParams1.rightMargin = LocaleController.isRTL ? AndroidUtilities.dp(16) : 0;
        layoutParams1.gravity = Gravity.TOP | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        avatarImage.setLayoutParams(layoutParams1);
        if (!isBroadcast) {
            avatarDrawable.setDrawPhoto(true);
            avatarImage.setOnClickListener(new View.OnClickListener() {
                // 카메라 그림을 클릭했을 때
                @Override
                public void onClick(View view) {
                    if (getParentActivity() == null) {
                        return;
                    }
                    // 나중에 "사진 촬영", "앨범" 선택 AlertDialog를 보여줄 객체를 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());

                    CharSequence[] items;

                    // FromCamera: "사진 촬영" , FromGallery: "앨범", DeletePhoto: "사진 삭제"
                    if (avatar != null) {
                        // 그룹 이미지가 있으면 "사진 삭제" 까지 AlertDialog 에서 보여줌
                        items = new CharSequence[]{LocaleController.getString("FromCamera", R.string.FromCamera), LocaleController.getString("FromGalley", R.string.FromGalley), LocaleController.getString("DeletePhoto", R.string.DeletePhoto)};
                    } else {
                        // 그룹 이미지가 없으면
                        items = new CharSequence[]{LocaleController.getString("FromCamera", R.string.FromCamera), LocaleController.getString("FromGalley", R.string.FromGalley)};
                    }

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                // "사진 촬영"
                                avatarUpdater.openCamera();
                            } else if (i == 1) {
                                // "앨범"
                                avatarUpdater.openGallery();
                            } else if (i == 2) {
                                // "사진 삭제" , TLRPC은 파일관련 클래스?
                                avatar = null;
                                uploadedAvatar = null;
                                avatarImage.setImage(avatar, "50_50", avatarDrawable);
                            }
                        }
                    });
                    showDialog(builder.create());
                }
            });
        }

        nameTextView = new EditText(context);
        // EnterListName: "리스트 이름을 입력하세요", EnterGroupNamePlaceholder: "그룹 이름 입력"
        nameTextView.setHint(isBroadcast ? LocaleController.getString("EnterListName", R.string.EnterListName) : LocaleController.getString("EnterGroupNamePlaceholder", R.string.EnterGroupNamePlaceholder));
        if (nameToSet != null) {
            nameTextView.setText(nameToSet);
            nameToSet = null;
        }
        nameTextView.setMaxLines(4);
        nameTextView.setGravity(Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT));
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        nameTextView.setHintTextColor(0xff979797);
        nameTextView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        nameTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        nameTextView.setPadding(0, 0, 0, AndroidUtilities.dp(8));
        AndroidUtilities.clearCursorDrawable(nameTextView);
        nameTextView.setTextColor(0xff212121);
        frameLayout.addView(nameTextView);
        layoutParams1 = (FrameLayout.LayoutParams) nameTextView.getLayoutParams();
        layoutParams1.width = LayoutHelper.MATCH_PARENT;
        layoutParams1.height = LayoutHelper.WRAP_CONTENT;
        layoutParams1.leftMargin = LocaleController.isRTL ? AndroidUtilities.dp(16) : AndroidUtilities.dp(96);
        layoutParams1.rightMargin = LocaleController.isRTL ? AndroidUtilities.dp(96) : AndroidUtilities.dp(16);
        layoutParams1.gravity = Gravity.CENTER_VERTICAL;
        nameTextView.setLayoutParams(layoutParams1);
        if (!isBroadcast) {
            nameTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    avatarDrawable.setInfo(5, nameTextView.length() > 0 ? nameTextView.getText().toString() : null, null, isBroadcast);
                    avatarImage.invalidate();
                }
            });
        }

        GreySectionCell sectionCell = new GreySectionCell(context);
        sectionCell.setText(LocaleController.formatPluralString("Members", selectedContacts.size()));
        linearLayout.addView(sectionCell);

        listView = new ListView(context);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter = new ListAdapter(context));
        linearLayout.addView(listView);
        layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.height = LayoutHelper.MATCH_PARENT;
        listView.setLayoutParams(layoutParams);

        return fragmentView;
    }

    // 사진을 올리는 클래스
    @Override
    public void didUploadedPhoto(final TLRPC.InputFile file, final TLRPC.PhotoSize small, final TLRPC.PhotoSize big) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                uploadedAvatar = file;
                avatar = small.location;
                avatarImage.setImage(avatar, "50_50", avatarDrawable);
                if (createAfterUpload) {
                    FileLog.e("tmessages", "avatar did uploaded");
                    MessagesController.getInstance().createChat(nameTextView.getText().toString(), selectedContacts, false);
                }
            }
        });
    }

    // 휴대폰 각도를 틀어서 사진을 찍어도 제대로 나오게
    @Override
    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        avatarUpdater.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void saveSelfArgs(Bundle args) {
        if (avatarUpdater != null && avatarUpdater.currentPicturePath != null) {
            // 상태 사진이 널이 아니고 경로도 널이 아니면
            args.putString("path", avatarUpdater.currentPicturePath);
        }
        if (nameTextView != null) {
            // 그룹 이름이 널이 아니라면
            String text = nameTextView.getText().toString();
            if (text != null && text.length() != 0) {
                args.putString("nameTextView", text);
            }
        }
    }

    // 리스토어
    @Override
    public void restoreSelfArgs(Bundle args) {
        if (avatarUpdater != null) {
            avatarUpdater.currentPicturePath = args.getString("path");
        }
        String text = args.getString("nameTextView");
        if (text != null) {
            if (nameTextView != null) {
                nameTextView.setText(text);
            } else {
                nameToSet = text;
            }
        }
    }

    // 그룹 이름 입력에 키보드로 입력된 글자 표시 <-> ChangeChatNameActivity.java 에서 onOpenAnimationEnd()가 상태 이미지에 키보드 입력 첫번째 글자 표시
    @Override
    public void onOpenAnimationEnd() {
        nameTextView.requestFocus();
        AndroidUtilities.showKeyboard(nameTextView);
    }

    @Override
    public void didReceivedNotification(int id, final Object... args) {
        if (id == NotificationCenter.updateInterfaces) {
            int mask = (Integer)args[0];
            if ((mask & MessagesController.UPDATE_MASK_AVATAR) != 0 || (mask & MessagesController.UPDATE_MASK_NAME) != 0 || (mask & MessagesController.UPDATE_MASK_STATUS) != 0) {
                updateVisibleRows(mask);
            }
        } else if (id == NotificationCenter.chatDidFailCreate) {
            // 채팅방 생성이 실패되었으면
            if (progressDialog != null) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e("tmessages", e);
                }
            }
            donePressed = false;
        } else if (id == NotificationCenter.chatDidCreated) {
            // 채팅방이 성공적으로 생성되었으면
            if (progressDialog != null) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    FileLog.e("tmessages", e);
                }
            }
            // 채팅방 아이디의 개수 배열
            int chat_id = (Integer)args[0];
            NotificationCenter.getInstance().postNotificationName(NotificationCenter.closeChats);
            Bundle args2 = new Bundle();
            // 채팅방 아이디를 bundle에 넣고 uploadedAvatar도 null이 아니면 chat_id와 함께 매칭시켜 넣는다. 그리고 ChatActivity를 실행시킨다.
            // ChatActivity는 BaseFragment를 상속 받는다.
            args2.putInt("chat_id", chat_id);

            // BaseFragment의 presentFragment 메소드 안에 parentLayout.presentFragment는 ActionBarLayout 클래스의 presentFragment
            presentFragment(new ChatActivity(args2), true);

            // uploadedAvatar가 null이 아니면 상태 그림을 바꿔줌
            if (uploadedAvatar != null) {
                // getInstance()를 실행시켜 MessagesController 객체를 만듬
                MessagesController.getInstance().changeChatAvatar(chat_id, uploadedAvatar);
            }
        }
    }

    // 대화상대 리스트
    private void updateVisibleRows(int mask) {
        if (listView == null) {
            return;
        }
        int count = listView.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = listView.getChildAt(a);
            if (child instanceof UserCell) {
                ((UserCell) child).update(mask);
            }
        }
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new UserCell(mContext, 1);
            }

            TLRPC.User user = MessagesController.getInstance().getUser(selectedContacts.get(i));
            ((UserCell) view).setData(user, null, null, 0);
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getCount() {
            return selectedContacts.size();
        }
    }
}
