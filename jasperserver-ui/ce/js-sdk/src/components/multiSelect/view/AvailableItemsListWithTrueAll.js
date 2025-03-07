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
import AvailableItemsList from '../view/AvailableItemsList';
import listWithTrueAllSelectionTrait from '../mixin/listWithTrueAllSelectionTrait';
var AvailableItemsListWithTrueAll = AvailableItemsList.extend({
    initialize: function (options) {
        if (options.trueAll) {
            delete options.value;
        }
        AvailableItemsList.prototype.initialize.call(this, options);
        this.setTrueAll(options.trueAll);
    },
    _createListView: function (options) {
        var listView = AvailableItemsList.prototype._createListView.call(this, options);
        return _.extend(listView, listWithTrueAllSelectionTrait);
    },
    initListeners: function () {
        AvailableItemsList.prototype.initListeners.call(this);
        this.listenTo(this.model, 'change:isTrueAll', this.changeTrueAll, this);
    },
    listRenderError: function (responseStatus, error) {
        if (this._boundedOnSelectionChangeOnce) {
            this.listViewModel.off('selection:change', this._boundedOnSelectionChangeOnce);
        }
        this.trigger('listRenderError', responseStatus, error);
    },
    selectionAdd: function (selection) {
        if (this.model.get('isTrueAll')) {
            this.model.set('isTrueAll', false, { silent: true });
            var visibleItems = _.chain(this.listViewModel.get('items')).map(function (item) {
                return item.value;
            }).reject(function (item) {
                return item === selection.value;
            }).value();
            this.listView.setTrueAll(false, { silent: true });
            this.listView.setValue(visibleItems, { silent: true });
            this._boundedOnSelectionChangeOnce = _.bind(this._onSelectionChangeOnce, this, selection);
            this.listViewModel.once('selection:change', this._boundedOnSelectionChangeOnce);
            this.listView.selectAll({ silent: true });
        } else {
            this.model.get('value')[selection.value] = true;
            this.model.trigger('change:value');
        }
    },
    _onSelectionChangeOnce: function (selection) {
        this.listViewModel.once('selection:remove', function () {
            this.processSelectionThroughApi(this.listView.getValue());
        }, this);
        this.listViewModel.toggleSelection(selection.value, selection.index);
    },
    onSelectAll: function () {
        if (!this.model.get('isTrueAll')) {
            this.model.set('isTrueAll', true);
        }
    },
    onSelectNone: function () {
        this.model.set('isTrueAll', false);
        AvailableItemsList.prototype.onSelectNone.call(this);
    },
    onInvertSelection: function () {
        if (this.model.get('isTrueAll')) {
            this.onSelectNone();
        } else if (_.isEmpty(this.model.get('value'))) {
            this.onSelectAll();
        } else {
            AvailableItemsList.prototype.onInvertSelection.call(this);
        }
    },
    changeTrueAll: function () {
        var isTrueAll = this.model.get('isTrueAll');
        if (isTrueAll) {
            this.listView.activate(undefined);
            this.listView.reset();
            var self = this;
            this.clearFilter(function () {
                self.listView.once('selection:change', self.processSelectionThroughApi, self);
                self.listView.setTrueAll(true);
            });
        } else {
            this.model.trigger('change:value');
            this.listView.setTrueAll(false);
        }
    },
    getModelForRendering: function () {
        return _.extend(AvailableItemsList.prototype.getModelForRendering.call(this), { isTrueAll: this.model.get('isTrueAll') });
    },
    setTrueAll: function (all) {
        this.model.set('isTrueAll', all);
    },
    getTrueAll: function () {
        return this.model.get('isTrueAll');
    }
});
export default AvailableItemsListWithTrueAll;