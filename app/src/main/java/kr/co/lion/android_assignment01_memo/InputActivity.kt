package kr.co.lion.android_assignment01_memo

import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.lion.android_assignment01_memo.databinding.ActivityInputBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class InputActivity : AppCompatActivity() {

    lateinit var activityInputBinding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        setToolbar()
        setView()
    }

    // Toolbar 설정
    fun setToolbar(){
        activityInputBinding.apply {
            toolbarInput.apply {
                // 타이틀
                title = "메모 작성"
                // 뒤로 가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_input)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        // 메모 작성 완료 메뉴
                        R.id.menu_input_Done -> {
                            // 입력 유효성 검사 메서드
                            checkInput()
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        // textFieldInputTitle 에 키보드 올리기
        Util.showSoftInput(activityInputBinding.textFieldInputTitle, this@InputActivity)
    }

    // 입력 유효성 검사
    fun checkInput(){
        activityInputBinding.apply {
            // 입력된 메모 제목 가져오기
            val memoTitle = textFieldInputTitle.text.toString()
            if(memoTitle.trim().isEmpty()){
                Util.showInfoDialog(this@InputActivity, "제목 입력 오류", "제목을 입력해주세요."){ dialogInterface: DialogInterface, i: Int ->
                    Util.showSoftInput(textFieldInputTitle, this@InputActivity)
                }
                return
            }
            // 입력된 메모 내용 가져오기
            val memoContent = textFieldInputContent.text.toString()
            if(memoContent.trim().isEmpty()){
                Util.showInfoDialog(this@InputActivity, "내용 입력 오류", "내용을 입력해주세요."){ dialogInterface: DialogInterface, i: Int ->
                    Util.showSoftInput(textFieldInputContent, this@InputActivity)
                }
                return
            }

            // 데이터 저장 처리 메소드
            addMemoData()
            // Activity 종료
            finish()
        }
    }

    // 데이터 저장 처리
    fun addMemoData(){
        activityInputBinding.apply {
            // Memo 객체 추출
            val memo = Memo()

            // 메모 제목
            memo.memoTitle = textFieldInputTitle.text.toString()
            // 메모 내용
            memo.memoContent = textFieldInputContent.text.toString()
            // 메모 날짜
            memo.memoDate = getDate()

            // 리스트에 저장
            Util.memoList.add(memo)
        }
    }

    // 현재 날짜를 구해주는 메서드
    fun getDate() : String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nowDate = LocalDateTime.now()
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
            return nowDate.format(dateFormatter)
        } else {
            val currentDate = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault()).format(
                Date()
            )
            return currentDate
        }
    }
}