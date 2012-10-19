package jpegmeta.client;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class MetaGroup {
	private final String name;
	private final String description;
	private final Map<MetaProperty, Object> properties = Maps.newHashMap();

	public MetaGroup(String name, String description) {
		this.name = Strings.nullToEmpty(name);
		this.description = Strings.nullToEmpty(description);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<MetaProperty, Object> getProperties() {
		return ImmutableMap.copyOf(this.properties);
	}

	public boolean contains(MetaProperty key) {
		return this.properties.containsKey(key);
	}

	public int size() {
		return this.properties.size();
	}

	/**
	 * 
	 * @param <T>
	 * @param key
	 * @param clazz
	 * @throws ClassCastException
	 *             if the retrieved value cannot be cast
	 * @return
	 */
	public <T> T getValue(MetaProperty key, Class<T> clazz) {
		return (T) this.properties.get(key);
	}

	public void addProperty(MetaProperty property, Object value) {
		Preconditions.checkNotNull(property);
		this.properties.put(property, value);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(MetaGroup.class).add("name", getName())
				.add("description", getDescription()).add("properties",
						this.properties.size()).toString();
	}
}
