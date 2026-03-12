<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人姓名" prop="elderlyName">
        <el-input
          v-model="queryParams.elderlyName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="约定服务时间" prop="serviceTime">
        <el-date-picker clearable
          v-model="queryParams.serviceTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择约定服务时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="服务地址" prop="serviceAddress">
        <el-input
          v-model="queryParams.serviceAddress"
          placeholder="请输入服务地址"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['order:serviceOrder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:serviceOrder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:serviceOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['order:serviceOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="服务商" align="center" prop="providerName" />
      <el-table-column label="服务项目" align="center" prop="itemName" />
      <el-table-column label="订单金额" align="center" prop="orderAmount" />
      <el-table-column label="约定服务时间" align="center" prop="serviceTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.serviceTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务地址" align="center" prop="serviceAddress" />
      <el-table-column label="服务要求说明" align="center" prop="serviceRequirements" />
      <el-table-column label="订单状态" align="center" prop="orderStatus">
        <template #default="scope">
          <dict-tag :options="order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="接单时间" align="center" prop="acceptTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.acceptTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:serviceOrder:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['order:serviceOrder:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改服务订单对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="serviceOrderRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select v-model="form.elderlyId" filterable remote reserve-keyword
            placeholder="请输入老人姓名搜索" :remote-method="remoteSearchElderly"
            :loading="elderlyLoading" style="width: 100%">
            <el-option v-for="item in elderlyOptions" :key="item.elderlyId"
              :label="item.name" :value="item.elderlyId" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联服务商" prop="providerId">
          <el-select v-model="form.providerId" filterable remote reserve-keyword
            placeholder="请输入服务商名称搜索" :remote-method="remoteSearchProvider"
            :loading="providerLoading" style="width: 100%">
            <el-option v-for="item in providerOptions" :key="item.providerId"
              :label="item.providerName" :value="item.providerId" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务项目" prop="serviceItemId">
          <el-select v-model="form.serviceItemId" filterable remote reserve-keyword
            placeholder="请输入服务项目名称搜索" :remote-method="remoteSearchItem"
            :loading="itemLoading" style="width: 100%">
            <el-option v-for="item in itemOptions" :key="item.itemId"
              :label="item.itemName" :value="item.itemId" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="订单金额" prop="orderAmount">
          <el-input v-model="form.orderAmount" placeholder="请输入订单金额" />
        </el-form-item>
        <el-form-item label="约定服务时间" prop="serviceTime">
          <el-date-picker clearable
            v-model="form.serviceTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择约定服务时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务要求说明" prop="serviceRequirements">
          <el-input v-model="form.serviceRequirements" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="订单状态" prop="orderStatus">
          <el-select v-model="form.orderStatus" placeholder="请选择订单状态">
            <el-option
              v-for="dict in order_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接单时间" prop="acceptTime">
          <el-date-picker clearable
            v-model="form.acceptTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择接单时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择服务开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务完成时间" prop="completeTime">
          <el-date-picker clearable
            v-model="form.completeTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择服务完成时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ServiceOrder">
import { listServiceOrder, getServiceOrder, delServiceOrder, addServiceOrder, updateServiceOrder } from "@/api/order/serviceOrder"
import { listElderly } from "@/api/elderly/elderly"
import { listServiceProvider } from "@/api/service/serviceProvider"
import { listServiceItem } from "@/api/service/serviceItem"

const { proxy } = getCurrentInstance()
const { order_status } = proxy.useDict('order_status')

const serviceOrderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const elderlyOptions = ref([])
const elderlyLoading = ref(false)
const providerOptions = ref([])
const providerLoading = ref(false)
const itemOptions = ref([])
const itemLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    providerId: null,
    serviceItemId: null,
    orderNo: null,
    serviceTime: null,
    serviceAddress: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    providerId: [
      { required: true, message: "关联服务商ID不能为空", trigger: "blur" }
    ],
    serviceItemId: [
      { required: true, message: "关联服务项目ID不能为空", trigger: "blur" }
    ],
    orderNo: [
      { required: true, message: "订单编号不能为空", trigger: "blur" }
    ],
    orderAmount: [
      { required: true, message: "订单金额不能为空", trigger: "blur" }
    ],
    serviceTime: [
      { required: true, message: "约定服务时间不能为空", trigger: "blur" }
    ],
    orderStatus: [
      { required: true, message: "订单状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务订单列表 */
function getList() {
  loading.value = true
  listServiceOrder(queryParams.value).then(response => {
    serviceOrderList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    orderId: null,
    elderlyId: null,
    providerId: null,
    serviceItemId: null,
    orderNo: null,
    orderAmount: null,
    serviceTime: null,
    serviceAddress: null,
    serviceRequirements: null,
    orderStatus: null,
    acceptTime: null,
    startTime: null,
    completeTime: null,
    cancelReason: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("serviceOrderRef")
}

/** 远程搜索老人 */
function remoteSearchElderly(query) {
  if (query) {
    elderlyLoading.value = true
    listElderly({ name: query, pageNum: 1, pageSize: 20 }).then(res => {
      elderlyOptions.value = res.rows
      elderlyLoading.value = false
    })
  } else { elderlyOptions.value = [] }
}

/** 远程搜索服务商 */
function remoteSearchProvider(query) {
  if (query) {
    providerLoading.value = true
    listServiceProvider({ providerName: query, pageNum: 1, pageSize: 20 }).then(res => {
      providerOptions.value = res.rows
      providerLoading.value = false
    })
  } else { providerOptions.value = [] }
}

/** 远程搜索服务项目 */
function remoteSearchItem(query) {
  if (query) {
    itemLoading.value = true
    listServiceItem({ itemName: query, pageNum: 1, pageSize: 20 }).then(res => {
      itemOptions.value = res.rows
      itemLoading.value = false
    })
  } else { itemOptions.value = [] }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.orderId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加服务订单"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _orderId = row.orderId || ids.value
  getServiceOrder(_orderId).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => { elderlyOptions.value = res.rows })
    }
    if (form.value.providerId) {
      listServiceProvider({ providerId: form.value.providerId, pageNum: 1, pageSize: 10 }).then(res => { providerOptions.value = res.rows })
    }
    if (form.value.serviceItemId) {
      listServiceItem({ itemId: form.value.serviceItemId, pageNum: 1, pageSize: 10 }).then(res => { itemOptions.value = res.rows })
    }
    open.value = true
    title.value = "修改服务订单"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["serviceOrderRef"].validate(valid => {
    if (valid) {
      if (form.value.orderId != null) {
        updateServiceOrder(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addServiceOrder(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _orderIds = row.orderId || ids.value
  proxy.$modal.confirm('是否确认删除服务订单编号为"' + _orderIds + '"的数据项？').then(function() {
    return delServiceOrder(_orderIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('order/serviceOrder/export', {
    ...queryParams.value
  }, `serviceOrder_${new Date().getTime()}.xlsx`)
}

getList()
</script>
