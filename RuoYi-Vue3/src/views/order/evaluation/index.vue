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
      <el-form-item label="老人姓名" prop="elderlyName">
        <el-input
          v-model="queryParams.elderlyName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeCreateTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['order:evaluation:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:evaluation:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:evaluation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['order:evaluation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="evaluationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评价id" align="center" prop="evaluationId" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="星级评分" align="center" prop="starLevel" />
      <el-table-column label="评价内容" align="center" prop="evaluationContent" />
      <el-table-column label="照片凭证" align="center" prop="proofPhotos" />
      <el-table-column label="评价提交时间" align="center" prop="evaluationTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.evaluationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务商回复" align="center" prop="providerReply" />
      <el-table-column label="回复时间" align="center" prop="replyTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.replyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['order:evaluation:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['order:evaluation:remove']">删除</el-button>
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

    <!-- 添加或修改服务评价管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="evaluationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联订单" prop="orderId">
          <el-select v-model="form.orderId" filterable remote reserve-keyword
            placeholder="请输入订单编号搜索" :remote-method="remoteSearchOrder"
            :loading="orderLoading" style="width: 100%">
            <el-option v-for="item in orderOptions" :key="item.orderId"
              :label="item.orderNo" :value="item.orderId" />
          </el-select>
        </el-form-item>
        <el-form-item label="评价人" prop="elderlyId">
          <el-select v-model="form.elderlyId" filterable remote reserve-keyword
            placeholder="请输入老人姓名搜索" :remote-method="remoteSearchElderly"
            :loading="elderlyLoading" style="width: 100%">
            <el-option v-for="item in elderlyOptions" :key="item.elderlyId"
              :label="item.name" :value="item.elderlyId" />
          </el-select>
        </el-form-item>
        <el-form-item label="星级评分" prop="starLevel">
          <el-input v-model="form.starLevel" placeholder="请输入星级评分" />
        </el-form-item>
        <el-form-item label="评价内容">
          <editor v-model="form.evaluationContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="评价提交时间" prop="evaluationTime">
          <el-date-picker clearable
            v-model="form.evaluationTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择评价提交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务商回复" prop="providerReply">
          <el-input v-model="form.providerReply" type="textarea" placeholder="请输入内容" />
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

<script setup name="Evaluation">
import { listEvaluation, getEvaluation, delEvaluation, addEvaluation, updateEvaluation } from "@/api/order/evaluation"
import { listServiceOrder } from "@/api/order/serviceOrder"
import { listElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()

const evaluationList = ref([])
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
const elderlyOptions = ref([])
const elderlyLoading = ref(false)
const daterangeCreateTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderId: null,
    orderNo: null,
    elderlyId: null,
    elderlyName: null,
    createTime: null,
  },
  rules: {
    orderId: [
      { required: true, message: "关联服务订单ID不能为空", trigger: "blur" }
    ],
    elderlyId: [
      { required: true, message: "评价人ID不能为空", trigger: "blur" }
    ],
    starLevel: [
      { required: true, message: "星级评分不能为空", trigger: "blur" }
    ],
    evaluationTime: [
      { required: true, message: "评价提交时间不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询服务评价管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeCreateTime.value && '' != daterangeCreateTime.value) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0]
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1]
  }
  listEvaluation(queryParams.value).then(response => {
    evaluationList.value = response.rows
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
    evaluationId: null,
    orderId: null,
    elderlyId: null,
    starLevel: null,
    evaluationContent: null,
    proofPhotos: null,
    evaluationTime: null,
    providerReply: null,
    replyTime: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("evaluationRef")
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

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.evaluationId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加服务评价管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _evaluationId = row.evaluationId || ids.value
  getEvaluation(_evaluationId).then(response => {
    form.value = response.data
    if (form.value.orderId) {
      listServiceOrder({ orderId: form.value.orderId, pageNum: 1, pageSize: 10 }).then(res => { orderOptions.value = res.rows })
    }
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => { elderlyOptions.value = res.rows })
    }
    open.value = true
    title.value = "修改服务评价管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["evaluationRef"].validate(valid => {
    if (valid) {
      if (form.value.evaluationId != null) {
        updateEvaluation(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addEvaluation(form.value).then(response => {
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
  const _evaluationIds = row.evaluationId || ids.value
  proxy.$modal.confirm('是否确认删除服务评价管理编号为"' + _evaluationIds + '"的数据项？').then(function() {
    return delEvaluation(_evaluationIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('order/evaluation/export', {
    ...queryParams.value
  }, `evaluation_${new Date().getTime()}.xlsx`)
}

getList()
</script>
