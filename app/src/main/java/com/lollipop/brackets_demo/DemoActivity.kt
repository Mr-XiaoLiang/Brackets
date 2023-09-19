package com.lollipop.brackets_demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.brackets.core.Scope
import com.lollipop.brackets.core.Stateful
import com.lollipop.brackets.core.Stateless
import com.lollipop.brackets.core.dp
import com.lollipop.brackets.impl.Space
import com.lollipop.brackets.kit.BracketsFragment

class DemoActivity : AppCompatActivity(), BracketsFragment.ContentBuilder {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
    }

    override fun buildContent(fragment: BracketsFragment, scope: Scope) {
        scope.apply {
            RadioGroup {
                title = Stateless("单选框1")
                Radio {
                    title = Stateless("单选1号")
                    isChecked = Stateful(true)
                }
                Space {
                    size = Stateless(30.dp)
                }
                Radio {
                    title = Stateless("单选2号")
                }
                Radio {
                    title = Stateless("单选3号")
                }
                Radio {
                    title = Stateless("单选4号")
                }
            }
            Space {
                size = Stateless(30.dp)
            }
            RadioGroup {
                title = Stateless("单选框2")
                Radio {
                    title = Stateless("单选5号")
                    isChecked = Stateful(true)
                }
                Radio {
                    title = Stateless("单选6号")
                }
                Radio {
                    title = Stateless("单选7号")
                }
                Radio {
                    title = Stateless("单选8号")
                }
            }
        }
    }


}