/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("unused")

package net.mamoe.mirai.console.utils

import net.mamoe.mirai.console.encodeToString
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.utils.ResourceContainer.Companion.asResourceContainer
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.reflect.KClass

/**
 * 资源容器.
 *
 * 资源容器可能使用 [Class.getResourceAsStream], 也可能使用其他方式, 取决于实现方式.
 *
 * @see JvmPlugin [JvmPlugin] 实现 [ResourceContainer], 使用 [ResourceContainer.asResourceContainer]
 */
public interface ResourceContainer {
    /**
     * 获取一个资源文件
     */
    public fun getResourceAsStream(name: String): InputStream

    /**
     * 读取一个资源文件并以 [Charsets.UTF_8] 编码为 [String]
     */
    @JvmDefault
    public fun getResource(name: String): String = getResource(name, Charsets.UTF_8)

    /**
     * 读取一个资源文件并以 [charset] 编码为 [String]
     */
    @JvmDefault
    public fun getResource(name: String, charset: Charset): String =
        this.getResourceAsStream(name).use { it.readBytes() }.encodeToString(charset)

    public companion object {
        /**
         * 使用 [Class.getResourceAsStream] 读取资源文件
         */
        @JvmStatic
        @JvmName("create")
        public fun KClass<*>.asResourceContainer(): ResourceContainer = this.java.asResourceContainer()

        /**
         * 使用 [Class.getResourceAsStream] 读取资源文件
         */
        @JvmStatic
        @JvmName("create")
        public fun Class<*>.asResourceContainer(): ResourceContainer = ClassAsResourceContainer(this)
    }
}

private class ClassAsResourceContainer(
    private val clazz: Class<*>
) : ResourceContainer {
    override fun getResourceAsStream(name: String): InputStream = clazz.getResourceAsStream(name)
}