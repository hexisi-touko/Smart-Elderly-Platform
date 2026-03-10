<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务人员姓名" prop="staffName">
        <el-input
          v-model="queryParams.staffName"
          placeholder="请输入服务人员姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否主要服务人员" prop="isPrimary">
        <el-select v-model="queryParams.isPrimary" placeholder="请选择是否主要服务人员" clearable>
          <el-option
            v-for="dict in sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['order:orderStaff:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:orderStaff:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:orderStaff:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['order:orderStaff:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderStaffList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一标识" align="center" prop="id" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="服务人员姓名" align="center" prop="staffName" />
      <el-table-column label="是否主要服务人员" align="center" prop="isPrimary">
        <template #default="scope">
          <dict-tag :options="sys_yes_no" :value="scope.row.isPrimary"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:orderStaff:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['order:orderStaff:remove']">删除</el-button>
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

    <!-- 添加或修改订单-服务人员关联对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="orderStaffRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联订单" prop="orderId">
          <el-select v-model="form.orderId" filterable remote reserve-keyword
            placeholder="请输入订单编号搜索" :remote-method="remoteSearchOrder"
            :loading="orderLoading" style="width: 100%">
            <el-option v-for="item in orderOptions" :key="item.orderId"
              :label="item.orderNo" :value="item.orderId" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务人员" prop="staffId">
          <el-select v-model="form.staffId" filterable remote reserve-keyword
            placeholder="请输入服务人员姓名搜索" :remote-method="remoteSearchStaff"
            :loading="staffLoading" style="width: 100%">
            <el-option v-for="item in staffOptions" :key="item.staffId"
              :label="item.staffName" :value="item.staffId" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否主要服务人员" prop="isPrimary">
          <el-select v-model="form.isPrimary" placeholder="请选择是否主要服务人员">
            <el-option
              v-for="dict in sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
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

<script setup name="OrderStaff">
import { listOrderStaff, getOrderStaff, delOrderStaff, addOrderStaff, updateOrderStaff } from "@/api/order/orderStaff"
import { listServiceOrder } from "@/api/order/serviceOrder"
import { listServiceStaff } from "@/api/service/serviceStaff"

const { proxy } = getCurrentInstance()
const { sys_yes_no } = proxy.useDict('sys_yes_no')

const orderStaffList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const orderOptions = ref([])
const orderLoading = ref(false)
const staffOptions = ref([])
const staffLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderId: null,
    orderNo: null,
    staffId: null,
    staffName: null,
    isPrimary: null,
  },
  rules: {
    orderId: [
      { required: true, message: "关联订单ID不能为空", trigger: "blur" }
    ],
    staffId: [
      { required: true, message: "关联服务人员ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询订单-服务人员关联列表 */
function getList() {
  loading.value = true
  listOrderStaff(queryParams.value).then(response => {
    orderStaffList.value = response.rows
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
    id: null,
    orderId: null,
    staffId: null,
    isPrimary: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("orderStaffRef")
}

/** 远程搜索订单 */
function remoteSearchOrder(query) {
  if (query) {
    orderLoading.value = true
    listServiceOrder({ orderNo: query, pageNum: 1, pageSize: 20 }).then(res => {
      orderOptions.value = res.rows
      orderLoading.value = false
    })
  } else { orderOptions.value = [] }
}

/** 远程搜索服务人员 */
function remoteSearchStaff(query) {
  if (query) {
    staffLoading.value = true
    listServiceStaff({ staffName: query, pageNum: 1, pageSize: 20 }).then(res => {
      staffOptions.value = res.rows
      staffLoading.value = false
    })
  } else { staffOptions.value = [] }
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
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加订单-服务人员关联"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getOrderStaff(_id).then(response => {
    form.value = response.data
    if (form.value.orderId) {
      listServiceOrder({ orderId: form.value.orderId, pageNum: 1, pageSize: 10 }).then(res => { orderOptions.value = res.rows })
    }
    if (form.value.staffId) {
      listServiceStaff({ staffId: form.value.staffId, pageNum: 1, pageSize: 10 }).then(res => { staffOptions.value = res.rows })
    }
    open.value = true
    title.value = "修改订单-服务人员关联"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["orderStaffRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateOrderStaff(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addOrderStaff(form.value).then(response => {
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
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除订单-服务人员关联编号为"' + _ids + '"的数据项？').then(function() {
    return delOrderStaff(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('order/orderStaff/export', {
    ...queryParams.value
  }, `orderStaff_${new Date().getTime()}.xlsx`)
}

getList()
</script>
