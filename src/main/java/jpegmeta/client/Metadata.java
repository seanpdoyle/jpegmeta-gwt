package jpegmeta.client;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

public class Metadata {
	private final ImmutableSet<MetaGroup> groups;

	Metadata(Collection<? extends MetaGroup> groups) {
		this.groups = ImmutableSet.copyOf(groups);
	}

	public <T> T getProperty(MetaProperty property, Class<T> clazz) {
		T value = null;
		for (MetaGroup group : this.groups) {
			if (group.contains(property)) {
				value = group.getValue(property, clazz);
			}
		}
		return value;
	}

	public boolean contains(MetaProperty property) {
		boolean contains = false;
		for (MetaGroup group : this.groups) {
			if (group.contains(property)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public int size() {
		int size = 0;
		for (MetaGroup group : this.groups) {
			size += group.size();
		}
		return size;
	}
}
