/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package groovy.lang;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class annotation used for making a class immutable.
 * <p/>
 * It allows you to write code snippets like this:
 * <pre>
 * {@code @Immutable} class Customer {
 *     String first, last
 *     int age
 *     Date since
 *     Collection favItems
 * }
 * def d = new Date()
 * def c1 = new Customer(first:'Tom', last:'Jones', age:21, since:d, favItems:['Books', 'Games'])
 * def c2 = new Customer('Tom', 'Jones', 21, d, ['Books', 'Games'])
 * assert c1 == c2
 * </pre>
 * A class created in this way has the following characteristics:
 * <ul>
 * <li>The class is automatically made final.
 * <li>Properties automatically have private, final backing fields with getters.
 * Attempts to update the property will result in a {@code ReadOnlyPropertyException}.
 * <li>A map-based constructor is provided which allows you to set properties by name.
 * <li>A tuple-style constructor is provided which allows you to set properties in the same order as they are defined.
 * <li>Default {@code equals}, {@code hashCode} and {@code toString} methods are provided based on the property values.
 * Though not normally required, you may write your own implementations of these methods. For {@code equals} and {@code hashCode},
 * if you do write your own method, it is up to you to obey the general contract for {@code equals} methods and supply a corresponding matching {@code hashCode} method.
 * If you do provide one of these methods explicitly, the default implementation will be made available in a private
 * "underscore" variant which you can call. E.g., you could provide a (not very elegant) multi-line formatted
 * {@code toString} method for {@code Customer} above as follows:
 * <pre>
 *     String toString() {
 *        _toString().replaceAll(/\(/, '(\n\t').replaceAll(/\)/, '\n)').replaceAll(/, /, '\n\t')
 *    }
 * </pre>
 * If an "underscore" version of the respective method already exists, then no default implementation is provided.
 * <li>{@code Date}s, {@code Cloneable}s and arrays are defensively copied on the way in (constructor) and out (getters).
 * Arrays and {@code Cloneable} objects use the {@code clone} method. For your own classes,
 * it is up to you to define this method and use deep cloning if appropriate.
 * <li>{@code Collection}s and {@code Map}s are wrapped by immutable wrapper classes (but not deeply cloned!).
 * Attempts to update them will result in an {@code UnsupportedOperationException}.
 * <li>Fields that are enums or other {@code @Immutable} classes are allowed but for an
 * otherwise possible mutable property type, an error is thrown.
 * <li>You don't have to follow Groovy's normal property conventions, e.g. you can create an explicit private field and
 * then you can write explicit get and set methods. Such an approach, isn't currently prohibited (to give you some
 * wiggle room to get around these conventions) but any fields created in this way are deemed not to be part of the
 * significant state of the object and aren't factored into the {@code equals} or {@code hashCode} methods.
 * Similarly, you may use static properties (though usually this is discouraged) and these too will be ignored
 * as far as significant state is concerned. If you do break standard conventions, you do so at your own risk and
 * your objects may no longer be immutable. It is up to you to ensure that your objects remain immutable at least
 * to the extent expected in other parts of your program!
 * </ul>
 * <p/>
 * Immutable classes are particularly useful for functional and concurrent styles of programming
 * and for use as key values within maps.
 * <p/>
 * Limitations:
 * <ul>
 * <li>
 * As outlined above, Arrays and {@code Cloneable} objects use the {@code clone} method. For your own classes,
 * it is up to you to define this method and use deep cloning if appropriate.
 * </li>
 * <li>
 * As outlined above, {@code Collection}s and {@code Map}s are wrapped by immutable wrapper classes (but not deeply cloned!).
 * </li>
 * <li>
 * Currently {@code BigInteger} and {#code BigDecimal} are deemed immutable but see:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6348370
 * </li>
 * <li>
 * {@code java.awt.Color} is treated as immutable but is not final so while not normally used with child
 * classes, it isn't strictly immutable. Use at your own risk.
 * </li>
 * <li>
 * {@code java.util.Date} is treated as immutable but is not final so it isn't strictly immutable. Use at your own risk.
 * </li>
 * </ul>
 *
 * @author Paul King
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@GroovyASTTransformationClass("org.codehaus.groovy.transform.ImmutableASTTransformation")
public @interface Immutable {
}
