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

import $ from 'jquery';
import _ from 'underscore';
import OptionContainer from '../base/OptionContainer';
import menuOptionTemplate from './template/menuOptionTemplate.htm';
import menuContainerTemplate from './template/menuContainerTemplate.htm';

export default OptionContainer.extend({
    constructor: function (options, additionalSettings) {
        if (!options || !_.isArray(options) || options.length === 0) {
            throw new Error('Menu should have options');
        }
        additionalSettings || (additionalSettings = {});
        OptionContainer.call(this, {
            options: options,
            collection: additionalSettings.collection,
            mainTemplate: additionalSettings.menuContainerTemplate || menuContainerTemplate,
            optionTemplate: additionalSettings.menuOptionTemplate || menuOptionTemplate,
            toggle: additionalSettings.toggle,
            toggleClass: additionalSettings.toggleClass
        });
        this.maxSize = additionalSettings.maxSize;
        this._onConstructor && this._onConstructor(options, additionalSettings);
    },
    initialize: function () {
        OptionContainer.prototype.initialize.apply(this, arguments);
        $('body').append(this.$el);
        this._onInitialize && this._onInitialize();
    },
    show: function () {
        var res = OptionContainer.prototype.show.apply(this, arguments);
        this.applyMaxSize();
        return res;
    },
    applyMaxSize: function () {
        if (this.maxSize && this.options.length > this.maxSize) {
            this.$el.css({
                'overflow-y': 'auto',
                'max-height': this.maxSize * this.options[0].$el.height() + 'px'
            });
        }
    }
});