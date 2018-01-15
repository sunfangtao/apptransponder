package com.sft.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MtbhLock {
	private static Map<String, Lock> MAP = new HashMap<String, Lock>();
	private static Lock MAP_LOCK = new ReentrantLock(true);
	private static int SIZE = 20;

	private static void clear() {
		MAP_LOCK.lock();
		try {
			for (Lock lock : MAP.values()) {
				if (!lock.tryLock()) {
					continue;
				}
				lock.unlock();
			}
			MAP.clear();
		} finally {
			MAP_LOCK.unlock();
		}
	}

	private static Lock addLock(String id, Lock lock) {
		MAP_LOCK.lock();
		try {
			if (!MAP.containsKey(id)) {
				MAP.put(id, lock);
				return lock;
			}
			Lock localLock = (Lock) MAP.get(id);
			return localLock;
		} finally {
			if (MAP.size() > SIZE) {
				clear();
			}
			MAP_LOCK.unlock();
		}
	}

	public static Lock getLock(String id) throws RuntimeException {
		if (id != null) {
			Lock lock = (Lock) MAP.get(id);
			if (lock == null) {
				lock = new ReentrantLock(true);
				return addLock(id, lock);
			}
			return lock;
		}

		throw new RuntimeException(id + " is null..");
	}
}
