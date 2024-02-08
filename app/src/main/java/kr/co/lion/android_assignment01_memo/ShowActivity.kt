package kr.co.lion.android_assignment01_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.android_assignment01_memo.databinding.ActivityShowBinding

class ShowActivity : AppCompatActivity() {

    lateinit var activityShowBinding: ActivityShowBinding

    // ModifyActivity의 런처
    lateinit var modifyActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowBinding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(activityShowBinding.root)

        setLauncher()
        setToolbar()
        setView()
    }

    // Launcher 설정
    fun setLauncher(){
        // ModifyAcitivity의 런처
        val contractModify = ActivityResultContracts.StartActivityForResult()
        modifyActivityLauncher = registerForActivityResult(contractModify){

        }
    }

    override fun onResume() {
        super.onResume()
        // 다른 Activity에서 돌아올 경우 View를 다시 설정
        setView()
    }

    // Toolbar 설정
    fun setToolbar(){
        activityShowBinding.apply {
            toolbarShow.apply {
                // 타이틀
                title = "메모 보기"
                // 뒤로 가기
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                // 메뉴
                inflateMenu(R.menu.menu_show)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        // 메모 수정 메뉴
                        R.id.menu_show_Modify -> {
                            // ModifyActivity 실행
                            val modifyIntent = Intent(this@ShowActivity, ModifyActivity::class.java)

                            // 메모의 position 값을 저장
                            val position = intent.getIntExtra("position", 0)
                            modifyIntent.putExtra("position", position)

                            modifyActivityLauncher.launch(modifyIntent)
                        }
                        // 메모 삭제 메뉴
                        R.id.menu_show_Delete -> {
                            // 항목 순서값을 추출
                            val position = intent.getIntExtra("position", 0)

                            // 항목 번째 객체를 리스트에서 삭제
                            Util.memoList.removeAt(position)
                            // Activity 종료
                            finish()
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        activityShowBinding.apply {
            // 항목 순서값을 추출
            val position = intent.getIntExtra("position", 0)
            // position 번째 객체를 추출
            val memo = Util.memoList[position]

            textFieldShowTitle.setText(memo.memoTitle)
            textFieldShowDate.setText(memo.memoDate)
            textFieldShowContent.setText(memo.memoContent)
        }
    }
}