package sk.graphics.main;

import sk.general.ImageCodes;

import java.util.LinkedList;

public class LogTab extends PaintTab {


	private static LinkedList<String> status = new LinkedList<String>();

	public LogTab() {
		super(ImageCodes.LOG.get());
	}

	@Override
	public String[] getStrings() {
		return status.toArray(new String[MAX_LINES]);
	}

	public static void addStatus(String s) {
		synchronized (status) {
			status.addFirst(s);
			if (status.size() > MAX_LINES)
				status.removeLast();
		}
	}
}
