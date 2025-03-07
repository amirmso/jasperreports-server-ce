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

import _ from 'underscore';
import SingleSelectListModel from '../model/SingleSelectListModel';
import ListWithNavigation from '../../scalableList/view/ListWithNavigation';
var SingleSelectList = ListWithNavigation.extend({
    events: _.extend({}, ListWithNavigation.prototype.events, { 'mouseup li': 'onMouseup' }),
    initialize: function (options) {
        var model = options.model || new SingleSelectListModel(options);
        ListWithNavigation.prototype.initialize.call(this, _.extend({
            model: model,
            lazy: true,
            selection: {
                allowed: true,
                multiple: false
            }
        }, options));
    },
    onMouseup: function () {
        this.trigger('item:mouseup');
    },
    activate: function (index) {
        if (this.getCanActivate()) {
            var active = this.getActiveValue();
            if (active && active.index === index) {
                return;
            }
            this.model.once('selection:change', this._triggerSelectionChanged, this).activate(index);
        }
    }
});
export default SingleSelectList;