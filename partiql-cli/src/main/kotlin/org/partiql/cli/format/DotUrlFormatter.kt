/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at:
 *
 *      http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.partiql.cli.format

// TODO: Add this for the v1 APIs
// 
// import com.google.common.net.PercentEscaper
// import org.partiql.pig.runtime.DomainNode
// 
// internal object DotUrlFormatter : NodeFormatter {
// 
//     private const val URL_PREFIX = "https://dreampuf.github.io/GraphvizOnline/#"
//     private val EOL = System.lineSeparator()
// 
//     override fun format(input: DomainNode): String {
//         val graph = DotFormatter.format(input)
//         val escaper = PercentEscaper("", false)
//         val params = escaper.escape(graph)
//         return "$URL_PREFIX$params$EOL"
//     }
// }
