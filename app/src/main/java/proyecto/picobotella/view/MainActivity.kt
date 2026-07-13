package proyecto.picobotella.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        if (isFinishing) {
            (application as PicoBotellaApplication).releaseMediaPlayers()
        }
        super.onDestroy()
    }
}
