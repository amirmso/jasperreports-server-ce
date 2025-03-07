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

import i18n from '../../../i18n/AttributeBundle.properties';
import i18nMessageUtil from 'js-sdk/src/common/util/i18nMessage';
import validationRulesEnum from '../../../attributes/enum/validationRulesEnum';

var i18nMessage = i18nMessageUtil.extend({bundle: i18n});


export default [
    {
        required: true,
        msg: new i18nMessage('attributes.error.attribute.name.empty')
    },
    {
        maxLength: validationRulesEnum.MAX_ATTRIBUTE_NAME_LENGTH,
        msg: new i18nMessage('attributes.error.attribute.name.too.long', validationRulesEnum.MAX_ATTRIBUTE_NAME_LENGTH)
    },
    {
        doesNotContainSymbols: '\\\\/',
        msg: new i18nMessage('attributes.error.attribute.name.invalid')
    }
];