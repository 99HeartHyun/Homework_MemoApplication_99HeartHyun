package kr.co.lion.android_assignment01_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android_assignment01_memo.databinding.ActivityMainBinding
import kr.co.lion.android_assignment01_memo.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // InputActivity의 런처
    lateinit var inputActivityLauncher: ActivityResultLauncher<Intent>
    // ShowActivity의 런처
    lateinit var showActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setLauncher()
        setToolbar()
        setView()
    }

    // Launcher 설정
    fun setLauncher(){
        // InputActivity의 런처
        val contractInput = ActivityResultContracts.StartActivityForResult()
        inputActivityLauncher = registerForActivityResult(contractInput){

        }
        // ShowActivity의 런처
        val contractShow = ActivityResultContracts.StartActivityForResult()
        showActivityLauncher = registerForActivityResult(contractShow){

        }
    }

    override fun onResume() {
        super.onResume()
        activityMainBinding.apply {
            // RecyclerView 데이터 갱신
            recyclerViewMain.adapter?.notifyDataSetChanged()
        }
    }

    // Toolbar 설정
    fun setToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                // 타이틀
                title = "메모 관리"
                // 메뉴
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    // 메뉴 아이디로 분기
                    when(it.itemId){
                        // 메모 작성 메뉴
                        R.id.menu_main_Add -> {
                            // InputActivity 실행
                            val inputIntent = Intent(this@MainActivity, InputActivity::class.java)
                            inputActivityLauncher.launch(inputIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    // View 설정
    fun setView(){
        activityMainBinding.apply {
            // RecyclerView
            recyclerViewMain.apply {
                // Adapter
                adapter = RecyclerViewMainAdapter()
                // Layout 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                // 항목 구분선
                val decoration = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(decoration)
            }
        }
    }

    // RecyclerView의 Adapter
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>() {
        // ViewHolder
        inner class ViewHolderMain(rowMainBinding: RowMainBinding) :
            RecyclerView.ViewHolder(rowMainBinding.root) {
            val rowMainBinding: RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding
                // 항목 당 가로, 세로의 길이 설정
                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return Util.memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            // 항목을 눌렀을 때의 리스너
            holder.rowMainBinding.root.setOnClickListener {
                // ShowActivity 실행
                val showMemoIntent = Intent(this@MainActivity, ShowActivity::class.java)
                // 현재 번째의 순서값 담기
                showMemoIntent.putExtra("position", position)

                showActivityLauncher.launch(showMemoIntent)
            }

            // position 번째 객체를 추출
            val memo = Util.memoList[position]

            // RecyclerView 항목에 메모 제목, 내용, 날짜 설정
            holder.rowMainBinding.textViewRowMainTitle.text = memo.memoTitle
            holder.rowMainBinding.textViewRowMainDate.text = memo.memoDate
        }
    }
}