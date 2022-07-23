package com.kt.tv_test.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.ArrayDeque

@Navigator.Name("atv_fragment")
class ATVFragmentNavigator(
    private val mContext: Context,
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) : FragmentNavigator(mContext, mFragmentManager, mContainerId) {
    companion object {
        private const val TAG = "ATVFragmentNavigator"
        private const val KEY_BACK_STACK_IDS = "androidx-nav-fragment:navigator:backStackIds"
    }

    private val mBackStack = ArrayDeque<Int>()

    override fun popBackStack(): Boolean {
        Log.d(TAG, "popBackStack $mBackStack")

        if (mBackStack.isEmpty()) {
            return false
        }
        if (mFragmentManager.isStateSaved) {
            Log.i(
                ATVFragmentNavigator.TAG, "Ignoring popBackStack() call: FragmentManager has already"
                    + " saved its state"
            )
            return false
        }
        mFragmentManager.popBackStack(
            generateBackStackName(mBackStack.size, mBackStack.peekLast()),
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        mBackStack.removeLast()?.let {
            Log.d(TAG, "## popBackStack removeLast $it")
        }
        return true
    }

    override fun createDestination(): Destination {
        return Destination(this)
    }

    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?): NavDestination? {
        if (mFragmentManager.isStateSaved) {
            Log.i(
                ATVFragmentNavigator.TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state"
            )
            return null
        }
        mFragmentManager.fragments?.forEach {
            Log.d(TAG, "navigate fragments $it")
            Log.d(TAG, "navigate fragments ${it.tag}")
        }
        for (i in 0 until mFragmentManager.backStackEntryCount) {
            Log.d(TAG, "backstack entry : ${mFragmentManager.getBackStackEntryAt(i)}")
        }
        Log.d(TAG, "navigate destination ${destination.id}")
        Log.d(TAG, "navigate backstack $mBackStack")
        // destination tag
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        val ft = mFragmentManager.beginTransaction()
        val tag = destination.id.toString()

        //navOptions 처리
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        @IdRes val destId = destination.id
        val initialNavigation = mBackStack.isEmpty()
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
            && navOptions.shouldLaunchSingleTop()
            && mBackStack.peekLast() == destId)
        var isAdded = if (initialNavigation) {
            true
        }
        else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                Log.d(TAG, "## isSingleTopReplacement!")
                mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        }
        else {
            Log.d(TAG, "## isAdded!")
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        // primaryNavigationFragment가 존재하면 기존 primaryFragment hide 처리 (재생성 방지)
        val clearAll = args?.getBoolean("clearAll", false)
        val currentFragment = mFragmentManager.primaryNavigationFragment
        if (currentFragment != null && clearAll != false) {
            Log.d(TAG, "## ATVFragmentNavigator() ${currentFragment::class.simpleName} hide")
            ft.hide(currentFragment)
        }
        var fragment: Fragment = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
        if (isAdded) {
            Log.d(TAG, "navigate add")
            // add로 fragment 최초 생성 (add)
            fragment?.arguments = args
            ft.add(mContainerId, fragment, tag)
        }
        else if (isSingleTopReplacement) {
            // 이미 생성되어 있던 fragment라면 show
            Log.d(TAG, "navigate show")
            fragment?.arguments = args
            ft.add(mContainerId, fragment, tag)
        }

        // destination fragment를 primary로 설정
        ft.setPrimaryNavigationFragment(fragment)

        // transaction 관련 fragment 상태 변경 최적화
        ft.setReorderingAllowed(true)
        ft.commit()

        Log.d(TAG, "navigate fragments stack ${mFragmentManager.fragments}")
        Log.d(TAG, "navigate backstack $mBackStack")
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        }
        else {
            null
        }
    }

    override fun onSaveState(): Bundle? {
        val b = Bundle()
        val backStack = IntArray(mBackStack.size)
        var index = 0
        for (id in mBackStack) {
            backStack[index++] = id
        }
        b.putIntArray(ATVFragmentNavigator.KEY_BACK_STACK_IDS, backStack)
        return b
    }

    override fun onRestoreState(savedState: Bundle?) {
        if (savedState != null) {
            val backStack = savedState.getIntArray(ATVFragmentNavigator.KEY_BACK_STACK_IDS)
            if (backStack != null) {
                mBackStack.clear()
                for (destId in backStack) {
                    mBackStack.add(destId)
                }
            }
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
}