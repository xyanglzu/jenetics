/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.util;

import static org.jenetics.util.accumulators.accumulate;

import java.util.Iterator;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date: 2014-02-15 $</em>
 */
public class accumulatorsTest {

	static final class IntegerIterator implements Iterator<Integer> {
		private final int _length;
		private int _pos = 0;

		IntegerIterator(final int length) {
			_length = length;
		}

		@Override
		public boolean hasNext() {
			return _pos < _length;
		}

		@Override
		public Integer next() {
			return _pos++;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	static final class IntegerIterable implements Iterable<Integer> {
		private final int _length;

		IntegerIterable(final int length) {
			_length = length;
		}

		@Override
		public Iterator<Integer> iterator() {
			return new IntegerIterator(_length);
		}

	}

	@Test
	public void callSpeed() {
		final Accumulator<Integer> accumulator = new MappedAccumulator<Integer>() {};
		Timer timer = new Timer();
		timer.start();
		for (long i = 0, n = 100000000L; i < n; ++i) {
			accumulator.accumulate(null);
		}
		timer.stop();
		System.out.println(accumulator);
		System.out.println(timer);
	}



	@Test
	public void accumulate1() {
		final int SAMPLES = 1000;
		final MappedAccumulator<Integer> accumulator = new MappedAccumulator<Integer>(){};
		accumulate(new IntegerIterator(SAMPLES), accumulator);

		Assert.assertEquals(accumulator.getSamples(), SAMPLES);
	}

	@Test
	public void accumulate2() {
		final int SAMPLES = 1000;
		final MappedAccumulator<Integer> accumulator = new MappedAccumulator<Integer>(){};
		accumulate(new IntegerIterable(SAMPLES), accumulator);

		Assert.assertEquals(accumulator.getSamples(), SAMPLES);
	}

	@Test
	public void accumulate3() {
		final int SAMPLES = 1000;
		final MappedAccumulator<Integer> accumulator1 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator2 = new MappedAccumulator<Integer>(){};

		accumulate(
				new IntegerIterable(SAMPLES),
				accumulator1,
				accumulator2
			);

		Assert.assertEquals(accumulator1.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator2.getSamples(), SAMPLES);
	}

	@Test
	public void accumulate4() {
		final int SAMPLES = 1000;
		final MappedAccumulator<Integer> accumulator1 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator2 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator3 = new MappedAccumulator<Integer>(){};

		accumulate(
				new IntegerIterable(SAMPLES),
				accumulator1,
				accumulator2,
				accumulator3
			);

		Assert.assertEquals(accumulator1.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator2.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator3.getSamples(), SAMPLES);
	}

	@Test
	public void accumulate5() {
		final int SAMPLES = 1000;
		final MappedAccumulator<Integer> accumulator1 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator2 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator3 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator4 = new MappedAccumulator<Integer>(){};

		accumulate(
				new IntegerIterable(SAMPLES),
				accumulator1,
				accumulator2,
				accumulator3,
				accumulator4
			);

		Assert.assertEquals(accumulator1.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator2.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator3.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator4.getSamples(), SAMPLES);
	}

	@Test
	public void accumulate6() {
		final int SAMPLES = 1000;
		final MappedAccumulator<Integer> accumulator1 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator2 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator3 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator4 = new MappedAccumulator<Integer>(){};
		final MappedAccumulator<Integer> accumulator5 = new MappedAccumulator<Integer>(){};

		accumulate(
				new IntegerIterable(SAMPLES),
				accumulator1,
				accumulator2,
				accumulator3,
				accumulator4,
				accumulator5
			);

		Assert.assertEquals(accumulator1.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator2.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator3.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator4.getSamples(), SAMPLES);
		Assert.assertEquals(accumulator5.getSamples(), SAMPLES);
	}

	@Test
	public void accumulate7() {
		final Seq<String> data = Array.valueOf("-10", "1", "2", "3", "4", "5");
		final accumulators.Max<Integer> max = new accumulators.Max<>();
		final accumulators.Min<Integer> min = new accumulators.Min<>();
		accumulators.accumulate(
			data,
			max.map(functions.StringToInteger),
			min.map(functions.StringLength)
		);
		System.out.println(String.format(
			"Max value:  %s, min length: %s.", max.getMax(), min.getMin()
		));
	}

	@Test
	public void accumulateN() {
		final int SAMPLES = 1000;
		final Array<MappedAccumulator<Integer>> accumulators = new Array<>(10);
		for (int i = 0; i < accumulators.length(); ++i) {
			accumulators.set(i, new MappedAccumulator<Integer>(){});
		}

		accumulate(
				new IntegerIterable(SAMPLES),
				accumulators
			);

		for (MappedAccumulator<Integer> accumulator : accumulators) {
			Assert.assertEquals(accumulator.getSamples(), SAMPLES);
		}
	}

//	@Test
//	public void sum() {
//		final Integer64[] array = new Integer64[20];
//		for (int i = 0; i < array.length; ++i) {
//			array[i] = Integer64.valueOf(i);
//		}
//
//		final Accumulators.Sum<Integer64> sum = new Accumulators.Sum<Integer64>();
//		Accumulators.accumulate(Arrays.asList(array), sum);
//		Assert.assertEquals(sum.getSum(), Integer64.valueOf((20*19/2)));
//	}

}
