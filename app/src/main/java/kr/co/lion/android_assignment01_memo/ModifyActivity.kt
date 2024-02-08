package kr.co.lion.android_assignment01_memo

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.android_assignment01_memo.databinding.ActivityModifyBinding

class ModifyActivity : AppCompatActivity() {

    lateinit var activityModifyBinding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModifyBinding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(activityModifyBinding.root)

        setToolbar()
        setView()
    }

    // Toolbar 설정
    fun setToolbar(){
        activityModifyBinding.apply {
            toolbarModify.apply {
                // 타이틀
                title = "메모 수정"
                // 뒤로 가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_modify)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        // 메모 편집 완료 메뉴
                        R.id.menu_modify_Done -> {
                            // 입력 유효성 검사 메서드
                            checkModifyInput()
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        activityModifyBinding.apply {
            // 항목 순서값을 추출
            val position = intent.getIntExtra("position", 0)
            // position 번째 객체를 추출
            val memo = Util.memoList[position]

            textFieldModifyTitle.setText(memo.memoTitle)
            textFieldModifyContent.setText(memo.memoContent)
        }
    }

    // 입력 유효성 검사
    fun checkModifyInput(){
        activityModifyBinding.apply {
            // 수정된 메모 제목 가져오기
            val memoTitle = textFieldModifyTitle.text.toString()
            if(memoTitle.trim().isEmpty()){
                Util.showInfoDialog(this@ModifyActivity, "제목 수정 오류", "제목을 입력해주세요."){ dialogInterface: DialogInterface, i: Int ->
                    Util.showSoftInput(textFieldModifyTitle, this@ModifyActivity)
                }
                return
            }
            // 수정된 메모 내용 가져오기
            val memoContent = textFieldModifyContent.text.toString()
            if(memoContent.trim().isEmpty()){
                Util.showInfoDialog(this@ModifyActivity, "내용 수정 오류", "내용을 입력해주세요."){ dialogInterface: DialogInterface, i: Int ->
                    Util.showSoftInput(textFieldModifyContent, this@ModifyActivity)
                }
                return
            }

            // 데이터 수정 처리 메소드
            modifyMemoData()
            // Activity 종료
            finish()
        }
    }

    // 데이터 수정 처리
    fun modifyMemoData(){
        // 항목 순서값을 추출
        val position = intent.getIntExtra("position", 0)
        // position 번째 객체를 추출
        val memo = Util.memoList[position]

        activityModifyBinding.apply {
            // 수정된 메모 제목
            memo.memoTitle = textFieldModifyTitle.text.toString()
            // 수정된 메모 내용
            memo.memoContent = textFieldModifyContent.text.toString()
        }
    }
}