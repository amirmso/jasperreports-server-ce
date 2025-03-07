/*
 * Copyright (C) 2005 - 2020 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperserver.api.engine.jasperreports.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperserver.api.engine.jasperreports.util.RepositoryCacheMap.CacheObject;
import com.jaspersoft.jasperserver.api.metadata.common.domain.util.DataContainerStreamUtil;

import net.sf.jasperreports.extensions.DefaultExtensionsRegistry;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JarsClassLoader extends ClassLoader {
	private static final Log log = LogFactory.getLog(JarsClassLoader.class);

	private final JarURLStreamHandler urlStreamHandler;

	private final JarFile[] jars;
	
	private final ProtectionDomain protectionDomain;

	private boolean classLoadingEnabled;

	public JarsClassLoader(JarFile[] jars, ClassLoader parent) {
		this(jars, parent, JarsClassLoader.class.getProtectionDomain(), false);
	}

	public JarsClassLoader(JarFile[] jars, ClassLoader parent, ProtectionDomain protectionDomain, 
			boolean classLoadingEnabled) {
		super(parent);

		this.urlStreamHandler = new JarURLStreamHandler();
		this.jars = jars;
		this.protectionDomain = protectionDomain;
		this.classLoadingEnabled = classLoadingEnabled;
	}

	protected Class findClass(String name) throws ClassNotFoundException {
		if (!classLoadingEnabled) {
			return super.findClass(name);
		}
		
		String path = name.replace('.', '/').concat(".class");

		JarFileEntry entry = findPath(path);

		if (entry == null) {
			throw new ClassNotFoundException(name);
		}

		//TODO certificates, package

		byte[] classData;
		try {
			long size = entry.getSize();
			if (size >= 0) {
				classData = DataContainerStreamUtil.readData(entry.getInputStream(),
						(int) size);
			} else {
				classData = DataContainerStreamUtil.readData(entry.getInputStream());
			}
		} catch (IOException e) {
			log.debug(e, e);
			throw new ClassNotFoundException(name, e);
		}

		return defineClass(name, classData, 0, classData.length,
				protectionDomain);
	}

	protected JarFileEntry findPath(String path) {
		JarFileEntry entry = null;
		for (int i = 0; i < jars.length && entry == null; i++) {
			entry = getJarEntry(jars[i], path);
		}
		return entry;
	}

	protected URL findResource(String name) {
		if (!classLoadingEnabled && name.endsWith(".class")) {
			return null;
		}
		
		JarFileEntry entry = findPath(name);
		return entry == null ? null : createURL(name, entry);
	}

	protected Enumeration findResources(String name) throws IOException {
		if (!classLoadingEnabled && name.endsWith(".class")) {
			return Collections.emptyEnumeration();
		}
		
		Vector urls = new Vector();
		for (int i = 0; i < jars.length; i++) {
			JarFileEntry entry = getJarEntry(jars[i], name);
			if (entry != null) {
				urls.add(createURL(name, entry));
			}
		}
		return urls.elements();
	}

	protected static JarFileEntry getJarEntry(JarFile jar, String name) {
		if (name.startsWith("/")) {
			name = name.substring(1);
		}

		JarFileEntry jarEntry = null;
		JarEntry entry = jar.getJarEntry(name);
		if (entry != null) {
			jarEntry = new JarFileEntry(jar, entry);
		}

		return jarEntry;
	}

	protected URL createURL(String name, JarFileEntry entry) {
		if (!classLoadingEnabled && DefaultExtensionsRegistry.EXTENSION_RESOURCE_NAME.equals(name)) {
			try (InputStream dataStream = entry.getInputStream()) {
				byte[] overrideData = SecureJRExtensionsFilter.instance().filterExtensionProperties(dataStream);
				entry.setOverrideData(overrideData);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return urlStreamHandler.createURL(entry);
	}

	public boolean hasSameJarFiles(List<CacheObject> cacheJarFiles) {
		List<Object> jarFiles = cacheJarFiles.stream().map(
				cacheObject -> cacheObject.getObject()).collect(Collectors.toList());
		return jarFiles.equals(Arrays.asList(jars));
	}
}
