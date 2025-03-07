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
package com.jaspersoft.jasperserver.api.engine.jasperreports.service.impl;

import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.jaspersoft.jasperserver.api.metadata.common.service.impl.hibernate.persistent.RepoDataSource;

public class EhcacheEngineServiceListener implements PostUpdateEventListener,  BeanFactoryAware {

	BeanFactory factory = null;
	protected String ehcacheEngineServiceName;

	public String getEhcacheEngineServiceName() {
		return ehcacheEngineServiceName;
	}

	public void setEhcacheEngineServiceName(String name) {
		this.ehcacheEngineServiceName = name;
	}

	public EhcacheEngineService getEhcacheEngineService() {
		return (EhcacheEngineService)factory.getBean(ehcacheEngineServiceName);
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		Object e = event.getEntity();
		if (e instanceof RepoDataSource) {
			getEhcacheEngineService().clear();
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return false;
	}

	@Override
	public void setBeanFactory(BeanFactory factory) throws BeansException {
		this.factory = factory;
	}

}
